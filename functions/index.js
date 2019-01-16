const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// Realtime Database under the path /messages/:pushId/original
exports.sendNotification = functions.database.ref("/game/0/time").onUpdate((change) => {
  var time = change.after.val();
  if (time!=0 && time!=5) return false;

  var payload;
  if (time == 5) {
    payload = {
      notification: {
        title: "제한시간 알림",
        body: "제한시간이 5초 남았습니다."
      }
    };
  } else if (time == 0) {
    payload = {
      notification: {
        title: "제한시간 알림",
        body: "제한시간이 종료되었습니다."
      }
    };
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
