Lab Exceptions & Final Work
Overview

The objective of is to handle all exceptions that maybe generated by your program during run time and to add some additional functionality to the graphical user interface.
Requirements

The chat client application in its normal running  will encounter exceptions such as when the socket connect with the server is broken unexpectedly. Other exceptions might include not being able to read or write to the files on the disk or even writing to a file that already exists.

You are required to handle all exceptions that are generated during the running of your client. In other words, your chat client should not crash due to an  exception not being handled. For the purposes of this lab, we will divide exceptions into two types:

Critical Exceptions:

When these exceptions occur, you are required to log the errors to a log file called system.log and notify the user that your program is not able to complete the actions as required. Perhaps by using a pop up window. An example might be where the socket connection is broken, you need to inform the user that the connection no longer exists, and offer the option to reconnect. Hint: these exceptions are likely already being caught in your program but not being handled.

You must create the class:

SystemExceptionHandler

for handling these exceptions.

 Non Critical Exceptions:

You are required to create a custom exception class for handling message exceptions from the server. Occasionally, the server will return malformed messages, messages from friends that do no exists on the friendslist, a duplicate friend or an invalid IP address for a friend. These message will probably not cause and critical exceptions, however when they occuir you must throw a custom exception.

You are therefore required to create a

CustomException class which extends Exception 

and also a class: CustomExceptionHandler

which is used to handle the exception. For these exceptions, you are free to choose how the exceptions are handled, whether you notify the user or log the error. 
