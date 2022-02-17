# android-popup-overlay

## Features
- Shows a custom popup screen triggered from FCM
- Uses Broadcast receivers

## Steps to run
- Create a Firebase project and add the `google-services.json` file inside the `app/` directory
- Send data message from FCM using some nodejs or python script.
- Data payload must be of schema:
```json
"data": {
  "message": "Hello"
}
```
