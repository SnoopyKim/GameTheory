const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// notification for mafia timer
exports.sendTimerNotification = functions.database.ref("/game/0/time").onUpdate((change) => {
  var time = change.after.val();
  if (time!=0 && time!=30 && time=59) return false;

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

// notification for block timer
exports.sendAuctionNotification = functions.database.ref("/game/1/time").onUpdate((change) => {
  var time = change.after.val();
  if (time!=0 && time!=5 && time!=30 && time!=59) return false;

  var payload;
  switch (time) {
    case 0:
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
