# ChatSocketProgram-exercise


The network program (client application + server application) for queuing and sending notifications.
The program handles default exceptions and the implementation of one or more exceptions related to the validation of user input in the client application.


Client application features:

-connection to the server along with validation and exception handling
-downloading from the user the content of the notification (text message) and the time of sending the notification to the user
-displaying the notification received by the server

Server application features:

-support for multiple clients at the same time
-receiving notifications sent from the client application and queuing them on the server
-sending notifications to the client who saved it at the time specified by him
