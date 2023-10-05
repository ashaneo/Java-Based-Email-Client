// your index number
//200623P

//import libraries

import com.sun.mail.smtp.SMTPAddressFailedException;

import java.io.FileWriter;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.mail.*;
import java.io.IOException;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class EmailClient {

    public static void main(String[] args) throws IOException {

        //Starting the Email Client
        StartandShutDown.Start();

        try {
            //Creating objects of recipients by reading the file
            List Details = ReadtheFile.CreateObjects("clientList.txt");
            CreateObjects.CreateObject(Details);
        } catch (FileNotFoundException e) {
            File myObj = new File("clientList.txt.");
            myObj.createNewFile();
        }

        if (!(SendEmailSSL.AllMailsSent.size() == 0)) {
            //Obtaining the current date
            String Today = GetCurrentDate.GetDate();

            //Obtaining the last day which emails are sent
            String LastDay = CheckLastDate.GetLastDate(SendEmailSSL.AllMailsSent);

            //This condition is to check last date which mails are sent
            //If the last day is today, that means we have sent mails to recipients who have their birthdays today
            //So no need to send them mails
            //If the last day is not today, mails will be sent to people who have birthdays today
            if (!LastDay.equals(GetCurrentDate.GetFullDate())) {
                List ListofPeople1 = GetAdresses.GetOfficeFriends(Today);
                for (int i = 0; i < ListofPeople1.size(); i++) {
                    SendanEmail.SendMail((String) ListofPeople1.get(i), "Happy Birthday", "Wish you a happy Birthday. Subodha");
                }

                List ListofPeople2 = GetAdresses.GetPersonalFriends(Today);
                for (int i = 0; i < ListofPeople2.size(); i++) {
                    SendanEmail.SendMail((String) ListofPeople2.get(i), "Happy Birthday", "Hugs and love on your birthday. Subodha ");
                }
            }
        }

        //Getting the operation that should be done as a number
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter option type: \n"
                + "1 - Adding a new recipient\n"
                + "2 - Sending an email\n"
                + "3 - Printing out all the recipients who have birthdays\n"
                + "4 - Printing out details of all the emails sent\n"
                + "5 - Printing out the number of recipient objects in the application \n"
                + "-1 - Exit");

        boolean open = true;
        int option = 0;
        while (open) {
            System.out.println("Enter the condition: ");
            String option1 = scanner.nextLine();
            try {
                option = Integer.parseInt(option1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input, please try again and enter the input according to instructions");
                System.out.println("Thank You!");
            }

        }
        ;
        switch (option) {
            //Case to shut down the client
            case -1:
                StartandShutDown.ShutDown();
                open = false;
                break;

            //Case to add recipients
            case 1:
                boolean open1 = true;
                while (open1) {
                    //Obtaining data about recipients to be added
                    Scanner scanner5 = new Scanner(System.in);


                    String checked = null;
                    Scanner scanner6 = new Scanner(System.in);
                    System.out.println("Enter recipient details separately"); //Obtaining input from the user
                    String s = scanner5.nextLine();

                    //If newly added receipients have BDays today, send them mails wishing for birthday
                    CheckNewReceipients.CheckRecipients(s);

                    //Checking whether the format of data entered is correct
                    CheckInputFormat newrecipient = new CheckInputFormat();
                    checked = newrecipient.checkformat(s);

                    //Writing entered data to the txt file
                    WritetoFile addDetails = new WritetoFile();
                    addDetails.writingtext(checked, "clientList.txt.");

                    //Create recipient objects
                    CreateObjects.CreateObject(checked);
                    open1 = false;
                }
                break;

            case 2:
                // input format should be - email, subject, content
                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Receivers Email: ");
                String email = scanner1.nextLine();
                Scanner scanner2 = new Scanner(System.in);
                System.out.println("Subject: ");
                String subject = scanner2.nextLine();
                Scanner scanner3 = new Scanner(System.in);
                System.out.println("Content: ");
                String content = scanner3.nextLine();

                // code to send an email
                SendEmailSSL mymail = new SendEmailSSL();
                mymail.mailsender(email, subject, content);
                break;

            case 3:
                // input format should be- MM/DD (ex:08/14)
                Scanner scanner4 = new Scanner(System.in);
                System.out.println("Date of Birth(MM/DD): ");
                String Date = scanner4.nextLine();

                GetRecipientsByDate newNumber = new GetRecipientsByDate();
                newNumber.GetRecipients(Date);
                break;

            case 4:
                // input format - YYYY/MM/dd (ex: 2018/08/14)
                Scanner scanner7 = new Scanner(System.in);
                System.out.println("Date on which mails were sent: ");
                String DateGiven = scanner7.nextLine();

                //Calling for the list of send mail objects
                List SentMails = SendEmailSSL.GetMailInfo();
                //This is to print all the mails sent in a given date
                //A list of sent mail objects and wanted date should be passed
                GetAllMailsSentinaDay.PrintMailDetails(SentMails, DateGiven);
                break;

            case 5:
                // Storing number of each recipient
                int OfficialRecipientsNum = CreateObjects.GetOfficialRecipientsList().size();
                int OfficialFriendsNum = CreateObjects.GetOfficialFriendsList().size();
                int PersonalRecipientsNum = CreateObjects.GetPersonalRecipientsList().size();

                //Asking for the  type of recipient needed
                System.out.println("Select the type of recipient: \n1.official recipients \n2.official friends \n3.personal recipients \n4.Total number of recipients");
                Scanner scanner8 = new Scanner(System.in);
                System.out.println("Enter the number: ");
                int dd = Integer.parseInt(scanner8.nextLine());

                //Printing the number of recipients according to the input
                switch (dd) {
                    case 1:
                        System.out.println("There are " + OfficialRecipientsNum + " official recipients");
                        break;
                    case 2:
                        System.out.println("There are " + OfficialFriendsNum + " official friends");
                        break;
                    case 3:
                        System.out.println("There are " + PersonalRecipientsNum + " personal recipients");
                        break;
                    case 4:
                        int total = OfficialRecipientsNum + OfficialFriendsNum + PersonalRecipientsNum;
                        System.out.println("There are " + total + " recipients in total.");
                        break;
                }
                break;
            default:
                System.out.println("Invalid Input, please try again and enter the input according to instructions");
                System.out.println("Thank You!");
                break;
        }
    }
    }


class SendEmailSSL {
    //This Class connects the email client with the email address
    //And this class maintains a list including details of all sent mails
    String Date = GetCurrentDate.GetFullDate();
    public static List AllMailsSent = new ArrayList();

    public void mailsender(String recievermail, String subject, String content) {
        final String username = "subodha200623@gmail.com";
        final String password = "zwcrbekydnezuvnx";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("subodha200623@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recievermail)
            );

            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Mail Sent");
            StoreSentMails newMail = new StoreSentMails(recievermail,Date,subject,content);
            AllMailsSent.add(newMail);

        }
        catch (MessagingException e) {
            System.out.println("Invalid attempt in sending the mail, please try again");
        }
    }

    //This method is to return the list including details of all mails sent
    public static List GetMailInfo(){
        return AllMailsSent;
    }

    //This method is to print all mails sent
    public static void printSentMails(){
        for (int i = 0; i< AllMailsSent.size(); i++){
            for(int j=0; j<1;j++){
                StoreSentMails l = (StoreSentMails) AllMailsSent.get(i);
                System.out.print(l.Mail + " " + l.Date + " " + l.Content);
            }
            System.out.println("");
        }
    }

}


class ReadtheFile {
    //This class is to read a given textfile
    public static List<String> CreateObjects(String line) throws IOException {
        //This class is to read a given text file and return a list of lines
        FileReader TextFile = new FileReader(line);
        BufferedReader ReadTextFile = new BufferedReader(TextFile);
        List<String> FileLines = new ArrayList<>();
        String str;

        while ((str = ReadTextFile.readLine()) != null) {
            FileLines.add(str);
        }

        ReadTextFile.close();
        return FileLines;
    }
}

class WritetoFile {
    //This class to write a given string to a given file
    String input;

    public static void writingtext(String recipient_details, String FileLocation)  {
        try{
            //String recipient_details = The content that should be written to file;
            //The File location to which data should be written
            File my_file =new File(FileLocation);

            // f file is not already created it will be created
            if(!my_file.exists()){
                my_file.createNewFile();}

            //Append the content to file
            FileWriter fwo = new FileWriter(my_file,true);

            //The buffer method is used to optimize performances
            BufferedWriter buff_wri = new BufferedWriter(fwo);
            buff_wri.write(recipient_details+"\n");

            //Closing the buffer writer
            buff_wri.close();

            System.out.println("Recipient Successfully Added");

        }

        //Handling exceptions
        catch (FileNotFoundException exc){
            System.out.println("The File Can't Be Found");
        }

        catch(IOException excep){
            System.out.println("Sorry, An Exception occurred:" + excep);
            //Print the type of exception
            excep.printStackTrace();
        }
    }

}

class GetAllMailsSentinaDay {
    //This class is to print details about all mails sent in a given date
    private static int n = 0;
    public static void PrintMailDetails(List SentMailDetails, String Date){
        System.out.println(Date);
        for (int i=0;i<SentMailDetails.size();i++){
            StoreSentMails element = (StoreSentMails) SentMailDetails.get(i);
            if (element.Date.equals(Date)){
                n = n+1;
                if (n ==1){
                    System.out.println("Mails sent on " + Date + ":");}
                System.out.print(" " + n + "." + element.Mail + " ");
                System.out.println(element.Subject);
            }
        }
        if (n==0){
            System.out.println("No mails are sent on " + Date);
        }
    }
}

class GetRecipientsByDate {
    //This class is to return a list of recipients who have their birthdays on given date
    static List List1 = CreateObjects.OfficialFriendsList;
    static List List2 = CreateObjects.PersonalRecipientsList;
    int num = 0;

    public <List1> void GetRecipients(String Date){
        int len1 = List1.size();
        int len2 = List2.size();

        for(int i=0; i<len1;i++){
            OfficialFriends l = (OfficialFriends) List1.get(i);
            //System.out.println(l.DateandMonth);
            if(l.DateandMonth.equals(Date)) {
                num = num + 1;
                if(num == 1){
                    System.out.println("People who have birth days on " + Date + ":");
                }
                if(num >= 1){
                    System.out.println(" " + num + ". " + l.Name);
                }
            }
        }

        for(int i=0; i<len2;i++){
            PersonalRecipients l = (PersonalRecipients) List2.get(i);
            //System.out.println(l.DateandMonth + " " + Date);
            if(l.DateandMonth.equals(Date)){
                this.num = num + 1;
                if(num == 1){
                    System.out.println("People who have birth days on " + Date + ":");
                }
                if(num >= 1){
                    System.out.println(" " + num + ". " + l.Name);
                }
            }
        }

        if (num ==0) {
            System.out.println("There are no one having birth day on " + Date);
        }
    }
}

class CheckNewReceipients {
    //When a new recipient is added, his or her BDay can be today
    //This class checks that and if the day is today,
    //A mail is sent wishing Happy Birthday
    public static void CheckRecipients(String line){
        String[]List1=line.split(",");
        String[]List2=List1[0].split(":");

        if(List2[0].equals("Office_friend")){
            String Email = List1[1];
            String BirthDay = (List1[3]).substring(5);
            if (BirthDay.equals(GetCurrentDate.GetDate())){
                SendanEmail.SendMail(Email, "Happy Birthday", "Wish you a Happy Birthday. Subodha");
            }
        }

        else if(List2[0].equals("Personal")) {
            String Email = List1[2];
            String BirthDay = (List1[3]).substring(5);
            if (BirthDay.equals(GetCurrentDate.GetDate())) {
                SendanEmail.SendMail(Email, "Happy Birthday", "Hugs and love on your birthday. Subodha");
            }
        }
    }
}

class CheckInputFormat {
    //This class is to check the input format of the input
    //If the format is correct details will be written to file
    public static String  checkformat(String GivenInput){

        try{
            String[]List1=GivenInput.split(",");
            String[]List2=List1[0].split(":");
            String toAdd = null;

            if(List2[0].equals("Official")){
                String Name = List2[1];
                String Email = List1[1];
                String Designation = List1[2];
                ReadtheFile newObject = new ReadtheFile();
                toAdd = "Official:" + Name + "," + Email + "," + Designation;
                return toAdd;}


            else if(List2[0].equals("Office_friend")){
                String Name = List2[1];
                String Email = List1[1];
                String Designation = List1[2];
                String BirthDay = List1[3];
                toAdd ="Office_friend:" + Name +"," + Email + "," + Designation + ","+ BirthDay;
                return toAdd;
            }

            else if(List2[0].equals("Personal")){
                String Name = List2[1];
                String NickName = List1[1];
                String Email = List1[2];
                String BirthDay = List1[3];
                //WritetoFile addDetails = new WritetoFile();
                toAdd = "Personal:" + Name +"," + NickName + "," + Email + ","+ BirthDay;
                return toAdd;}

            else{
                System.out.println("Try Again");}

            return toAdd;}

        catch (NullPointerException ex){
            return null;
        }
    }
}

class SendanEmail {
    //This class is to send an email
    public static void SendMail(String email, String subject, String content){
        SendEmailSSL mail1 = new SendEmailSSL();
        mail1.mailsender(email,subject,content);
    }
}


class CheckLastDate {
    //This class is to check the last date in which mails are sent
    public static String GetLastDate(List MailDetails){
        int length = MailDetails.size();
        StoreSentMails lastMail = (StoreSentMails) MailDetails.get(length-1);
        String LastDate = lastMail.Date;
        return LastDate;
    }
}

class CreateObjects {
    //This class is to create recipient objects
    //This class saves details about all recipients in program
    public static List OfficialRecipientsList = new ArrayList<>();
    public static List OfficialFriendsList = new ArrayList<>();
    public static List PersonalRecipientsList = new ArrayList<>();

    public static List AllBirthdaysList = new ArrayList<>();

    public static void CreateObject(List<String> Details){
        int len = Details.size();
        String line = null;

        for (int i =0; i<len;i++){
            line = Details.get(i);
            //System.out.println(line);


            String[] List1=line.split(",");
            String[] List2=List1[0].split(":");


            if(List2[0].equals("Official")){
                String Name = List2[1];
                String Email = List1[1];
                String Designation = List1[2];
                OfficialRecipients recepient = new OfficialRecipients(Name,Designation,Email);
                OfficialRecipientsList.add(recepient);
            }

            else if(List2[0].equals("Office_friend")){
                String Name = List2[1];
                String Email = List1[1];
                String Designation = List1[2];
                String BirthDay = List1[3];
                OfficialFriends recepient = new OfficialFriends(Name,Designation,Email,BirthDay);
                OfficialFriendsList.add(recepient);
            }

            else if(List2[0].equals("Personal")){
                String Name = List2[1];
                String NickName = List1[1];
                String Email = List1[2];
                String BirthDay = List1[3];
                PersonalRecipients recepient = new PersonalRecipients(Name,NickName,Email,BirthDay);
                PersonalRecipientsList.add(recepient);
            }
        }}

    public static void CreateObject(String line){
        String[]List1=line.split(",");
        String[]List2=List1[0].split(":");

        if(List2[0].equals("Official")){
            String Name = List2[1];
            String Email = List1[1];
            String Designation = List1[2];
            OfficialRecipients recepient = new OfficialRecipients(Name,Designation,Email);
            OfficialRecipientsList.add(recepient);
        }

        else if(List2[0].equals("Office_friend")){
            String Name = List2[1];
            String Email = List1[1];
            String Designation = List1[2];
            String BirthDay = List1[3];
            OfficialFriends recepient = new OfficialFriends(Name,Designation,Email,BirthDay);
            OfficialFriendsList.add(recepient);
        }

        else if(List2[0].equals("Personal")){
            String Name = List2[1];
            String NickName = List1[1];
            String Email = List1[2];
            String BirthDay = List1[3];
            PersonalRecipients recepient = new PersonalRecipients(Name,NickName,Email,BirthDay);
            PersonalRecipientsList.add(recepient);
        }
    }

    public static List GetOfficialRecipientsList(){
        //This method is to get the list of official recipients when needed
        return OfficialRecipientsList;
    }

    public static List GetOfficialFriendsList(){
        //This method is to get the list of official friends when needed
        return OfficialFriendsList;
    }

    public static List GetPersonalRecipientsList(){
        //This method is to get the list of personal friends when needed
        return PersonalRecipientsList;
    }

}

abstract class Recipients {
    //This is a abstract class for all recipients
    String Name;
    String Email;
}

class OfficialRecipients extends Recipients {
    //Extended class for official recipients
    String Designation;

    //Constructor to create objects of official recipients
    OfficialRecipients(String name, String designation, String email){
        this.Name = name;
        this.Designation = designation;
        this.Email = email;
    }
}

class OfficialFriends extends Recipients {
    //Extended class for official friends
    String Designation;
    String DateandMonth;
    String Year;

    //Constructor to create objects of official friends
    OfficialFriends(String name, String designation, String email, String birthday) {
        this.Name = name;
        this.Designation = designation;
        this.Email = email;

        String[]List1=birthday.split("/");
        this.Year = List1[0];
        this.DateandMonth = List1[1] + "/" + List1[2];
    }
}

class PersonalRecipients extends Recipients{
    //Extended class for personal friends
    String NickName;
    String DateandMonth;
    String Year;

    //Constructor to create objects of personal friends
    PersonalRecipients(String name, String nickname, String email, String birthday) {
        this.Name = name;
        this.NickName = nickname;
        this.Email = email;

        String[]List1=birthday.split("/");
        this.Year = List1[0];
        this.DateandMonth = List1[1] + "/" + List1[2];
    }
}

class StoreSentMails implements Serializable {
    String Mail;
    String Date;
    String Subject;
    String Content;

    StoreSentMails (String email, String date, String subject, String content){
        this.Mail = email;
        this.Date = date;
        this.Subject = subject;
        this.Content = content;
    }
}

class StartandShutDown {
    //This class is to manage end and start of email client

    public static void Start(){
        //Method to be called in the beginning
        System.out.println("Hi! Hope you are doing well.");
        SerializingManager.deserialize();
    }

    public static void ShutDown(){
        //Method to be called when exiting
        System.out.println("Thanks for using my email client. \nHave a nice day");
        SerializingManager.Serialize();
    }
}
class GetCurrentDate {
    //This class is to return the current date
    //in a preferred format

    public static String GetDate(){
        LocalDate date = LocalDate.now();
        String FullDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String Date = FullDate.substring(5);
        return Date;
    }

    public static String GetFullDate(){
        LocalDate date = LocalDate.now();
        String FullDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return FullDate;
    }
}

class GetAdresses {
    //This class is to get mails of people who have
    //their birth days on a specific date
    static List List1 = CreateObjects.OfficialFriendsList;
    static List List2 = CreateObjects.PersonalRecipientsList;
    public static List ListofPeople1 = new ArrayList<>();
    public static List ListofPeople2 = new ArrayList<>();

    public static  <List1> List GetOfficeFriends(String Date){
        int len1 = List1.size();
        int len2 = List2.size();

        for(int i=0; i<len1;i++){
            OfficialFriends l = (OfficialFriends) List1.get(i);
            if(l.DateandMonth.equals(Date)) {
                ListofPeople1.add(l.Email);
            }
        }
        return ListofPeople1;
    }


    public static  <List1> List GetPersonalFriends(String Date){
        int len2 = List2.size();
        for(int i=0; i<len2;i++){
            PersonalRecipients l = (PersonalRecipients) List2.get(i);
            if(l.DateandMonth.equals(Date)){
                ListofPeople2.add(l.Email);
            }
        }
        return ListofPeople2;
    }
}

class SerializingManager {
    //Class to manage serializing send emails

    //The file where serialized objects are saved
    private static String filegiven = "SendEmail.ser";
    public static void Serialize(){
        FileOutputStream fs1 = null;
        ObjectOutputStream toOut = null;
        try
        {
            //Saving of object in a file
            fs1 = new FileOutputStream(filegiven);
            toOut = new ObjectOutputStream(fs1);

            // This is for serializing the object
            toOut.writeObject(SendEmailSSL.AllMailsSent);

            toOut.close();
            fs1.close();

        }

        //Handling exceptions
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally{
            try{
                if(toOut !=null){
                    toOut.close();
                }
                if(fs1 != null){
                    fs1.close();
                }
            } catch (IOException a) {
                a.printStackTrace();
            }
        }
    }

    public static void deserialize(){
        //This method is to deserialize
        FileInputStream fs2 = null;
        ObjectInputStream toIn = null;
        try
        {
            // Reading the object from a file
            fs2 = new FileInputStream(filegiven);
            toIn = new ObjectInputStream(fs2);

            // This will deserialize the objects
            List<StoreSentMails> object1 = (List<StoreSentMails>)toIn.readObject();

            SendEmailSSL.AllMailsSent.addAll(object1);
            toIn.close();
            fs2.close();
        }

        catch (FileNotFoundException ex){
            System.out.println("Welcome to email client for the first time");
            System.out.println("Have a good experience");
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        catch (ClassNotFoundException z) {
            throw new RuntimeException(z);
        }
        finally{
            try{
                if(toIn !=null){
                    toIn.close();
                }
                if(fs2 != null){
                    fs2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}