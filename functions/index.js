const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.database();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// notification for mafia timer
exports.sendTimerNotification = functions.database.ref("/game/0/time").onUpdate((change) => {
  var time = change.after.val();
  if (time!=0 && time!=30 && time!=59) return false;

  var payload;
  switch (time) {
    case 0:
      payload = {
        notification: {
          title: "턴 알림",
          body: "밤이 되었습니다."
        }
      };
      break;
    case 30:
      payload = {
        notification: {
          title: "제한시간 알림",
          body: "낮이 30초 남았습니다."
        }
      };
      break;
    case 59:
      payload = {
        notification: {
          title: "턴 알림",
          body: "낮이 되었습니다."
        }
      };
      break;
    default:
      break;
  }

  admin.messaging().sendToTopic("timer", payload)
    .then((response) => {
        console.log("Successfully sent message: ", response);
        return true;
    })
    .catch((error) => {
        console.log("Error sending message: ", error);
        return false;
    })
});

// notification for mafia vote
exports.sendVoteNotification = functions.database.ref("/game/0/vote/{key}").onCreate((snapshot, context) => {
  var voteData = snapshot.val();

  var payload = {
    notification: {
      title: "투표 알림",
      body: voteData.from+"님이 "+voteData.to+"에게 투표하였습니다. ("+voteData.time+")"
    }
  };

  admin.messaging().sendToTopic("timer", payload)
    .then((response) => {
        console.log("Successfully sent message: ", response);
        return true;
    })
    .catch((error) => {
        console.log("Error sending message: ", error);
        return false;
    })
});

let round = 1;
// notification for block timer
exports.sendAuctionTimerNotification = functions.database.ref("/game/1/time").onUpdate((change) => {
  var time = change.after.val();
  if (time!=0 && time!=5 && time!=30 && time!=59) return false;

  var payload;
  switch (time) {
    case 0:
      let money, count;
      let player, item, bid;
      db.ref("game/1/auction").once("value", (snapshot) => {
        uid = snapshot.val().player_uid;
        player = snapshot.val().player_name;
        item = snapshot.val().item;
        bid = snapshot.val().current_bid;
        if (player == "없음") {
          payload = {
            notification: {
              title: "낙찰 알림",
              body: "아무도 "+item+"를 입찰하지 않았습니다."
            }
          }
          db.ref("game/1/auction").remove();
        } else {
          payload = {
            notification: {
              title: "낙찰 알림",
              body: item+"이 "+player+"에게 "+bid+"에 낙찰되었습니다."
            }
          }
          db.ref("game/1/trades").child(round.toString()).set(
            {
              uid: uid,
              name: player,
              item: item,
              bid: bid
            }
          );
          db.ref("game/1/players").child(uid).once("value", (snapshot) => {
            money = snapshot.child("money").val();
            count = snapshot.child(item).val();
            db.ref("game/1/players").child(uid).child("money").set(money-bid);
            db.ref("game/1/players").child(uid).child(item).set(count/1+1);
          });
          db.ref("game/1/auction").remove();
        }
        admin.messaging().sendToTopic("timer", payload);
        round+=1;
      });
      payload = {
        notification: {
          title: "턴 알림",
          body: "경매가 종료되었습니다."
        }
      };
      break;
    case 5:
      payload = {
        notification: {
          title: "제한시간 알림",
          body: "경매 시간이 5초 남았습니다."
        }
      };
      break;
    case 30:
      payload = {
        notification: {
          title: "제한시간 알림",
          body: "경매시간이 30초 남았습니다."
        }
      };
      break;
    case 59:
      payload = {
        notification: {
          title: "턴 알림",
          body: "경매가 시작되었습니다."
        }
      };
      break;
    default:
      break;
  }

  admin.messaging().sendToTopic("timer", payload)
    .then((response) => {
        console.log("Successfully sent message: ", response);
        return true;
    })
    .catch((error) => {
        console.log("Error sending message: ", error);
        return false;
    })
});

// notification for block timer
exports.sendAuctionNotification = functions.database.ref("/game/1/auction").onUpdate((change) => {
  var player = change.after.val().player_name;
  var item = change.after.val().item;
  var current_bid = change.after.val().current_bid;
  var payload = {
    notification: {
      title: "경매 입찰 알림",
      body: player+"님이 "+item+"을 "+current_bid+"에 입찰하셨습니다."
    }
  };

  admin.messaging().sendToTopic("timer", payload)
    .then((response) => {
        console.log("Successfully sent message: ", response);
        return true;
    })
    .catch((error) => {
        console.log("Error sending message: ", error);
        return false;
    })
});
