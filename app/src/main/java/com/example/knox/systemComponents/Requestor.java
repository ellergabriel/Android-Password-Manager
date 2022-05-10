package com.example.knox.systemComponents;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.assist.AssistStructure;
import android.os.CancellationSignal;
import android.os.Parcel;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;


import androidx.annotation.NonNull;
import androidx.room.RawQuery;
import androidx.room.Room;

import com.example.knox.R;

public final class Requestor extends AutofillService {

    //Singleton creation pattern
    private static volatile Requestor instance = null;
    private static volatile long timer = -1;
    private static String capturedUName;
    private static String capturedPassword;
    public static String capturedURL;
    public Requestor(){} //constructor must be public to implement autofill service

    @Override
    public void onFillRequest(@NonNull FillRequest fillRequest, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback fillCallback) {
        /*todo:check if session timer has ran out
        if timer is out, show fingerprint prompt to user before continuing
        else, keep it going*/
        boolean hasLogin = false;
        //Structure from request
        List<FillContext> context = fillRequest.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();

        ParsedStructure parsedStruct = new ParsedStructure();

        //todo: add threading support so database is not called on main thread
        //      it works for now, but stresses the system
        Database db = Room.databaseBuilder(getApplicationContext(), Database.class,
                                            "credentials").allowMainThreadQueries().build();
        PasswordDAO dao = db.passDao();

        /*** Debugging section for database functionality
        dao.insertAll(new Credentials("shazbot", "shazbot", "shazbot.com"));
        List<Credentials> tester = dao.vaultDisplay();
        for(int i = 0; i < tester.size(); i++){
            System.out.println(tester.get(i));
        }
         *****/

        //fetching user data from AssistStructure
        parseStructure(structure, parsedStruct);

        RemoteViews userNamePresentation = new RemoteViews(this.getPackageName(), android.R.layout.simple_list_item_1);
        RemoteViews passwordPresentation = new RemoteViews(this.getPackageName(), android.R.layout.simple_list_item_1);
        //dao.insertAll(new Credentials("eller010", "password", parsedStruct.URL));
        Credentials cred = dao.getFullCred(parsedStruct.URL);

        if(cred == null){
            cred = new Credentials("DNE","DNE","");
        }

        //Creates **** password text so user does not see password on autofill
        int defaultSize = 6;
        String dummy = "";
        char holder = 46;
        for(int i = 0; i < defaultSize; i++){
            dummy += holder;
        }

        userNamePresentation.setTextViewText(android.R.id.text1, cred.getUName());
        passwordPresentation.setTextViewText(android.R.id.text1, dummy);
        //Adds dataset with credentials to response

        FillResponse fillResponse = new FillResponse.Builder()
                .addDataset(new Dataset.Builder()
                        .setValue(parsedStruct.userID,
                                AutofillValue.forText(cred.getUName()), userNamePresentation)
                        .setValue(parsedStruct.passID,
                                AutofillValue.forText(cred.getPasswd()), passwordPresentation)
                        .build())
                .setSaveInfo(new SaveInfo.Builder(
                        SaveInfo.SAVE_DATA_TYPE_USERNAME | SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                        new AutofillId[]{parsedStruct.userID, parsedStruct.passID})
                        .build())
                .build();

        fillCallback.onSuccess(fillResponse);


    }

    //todo: once database is implemented, make onSaveRequest encrypt and save to the database
    @Override
    public void onSaveRequest(@NonNull SaveRequest saveRequest, @NonNull SaveCallback saveCallback) {
        // Get the structure from the request
        List<FillContext> context = saveRequest.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();
        ParsedStructure parse = new ParsedStructure();
        // Traverse the structure looking for data to save
        parseStructure(structure, parse);
        if(capturedUName != null && capturedPassword != null
            && capturedURL != null){
            Credentials save = new Credentials(capturedUName, capturedPassword, capturedURL);
            capturedURL = capturedPassword = capturedUName = null;
            Database.getInstance(getApplicationContext()).insert(save);
        }
        // Persist the data, if there are no errors, call onSuccess()
        saveCallback.onSuccess();
    }

    public static long checkTimer(){
       return timer;
    }

    public static void setTimer(){
        timer = System.currentTimeMillis();
    }

    /**
     * Lazy Singleton constructor
     * @return single instance of Requestor
     */
    public static Requestor getInstance(){
        if(instance == null){
            instance = new Requestor();
            //timer = new Timer();
        }
        return instance;
    }

    /**
     * Function takes in AssistStructure, searches all WindowsNodes for ViewNodes, then searches for
     * autofill information in ViewNodes
     * @param struct AssistStructure that has AutofillID information
     * @param parser Object that encapsulates all found AutofillID information
     */
    private static void parseStructure(AssistStructure struct, ParsedStructure parser){
        int nodes = struct.getWindowNodeCount();
        //todo:
        for(int i = 0; i < nodes; i++){
            AssistStructure.WindowNode window = struct.getWindowNodeAt(i);
            AssistStructure.ViewNode view = window.getRootViewNode();
            traverseNode(view, parser);
        }//end for
    }

    /**
     * Recursive function to parse through ViewNode information via Children field
     *
     * @param viewNode child node to explore
     * @param parser
     */
    private static void traverseNode(AssistStructure.ViewNode viewNode, ParsedStructure parser){
        if (viewNode.getChildCount() > 0){
            AssistStructure.ViewNode child;
            for(int i = 0; i < viewNode.getChildCount(); i++){
                child = viewNode.getChildAt(i);
                String tester = child.getHint();
                if(child.getWebDomain() != null){ //webdomain will only be sent once, safe to assign
                                                 //to parsedStructure
                    parser.URL = child.getWebDomain();
                    capturedURL = child.getWebDomain();
                }
                if(child.getChildCount() > 0){
                    traverseNode(child, parser);//recursive call for now;
                    /*todo: implement queue when autofill is fully working
                            recursive function is not as optimized*/
                }
                if(!(child.getAutofillHints() == null) || !(tester == null)){
                    System.out.println("debug holder\n");
                    //text field has some hint, check for id
                    if(child.getHint().equals("Username")){
                        parser.userID = child.getAutofillId();
                        try {
                            capturedUName = (String) child.getAutofillValue().getTextValue();
                        } catch (NullPointerException n) { /*do nothing*/}
                    } else if (child.getHint().equals("Password")){
                        parser.passID = child.getAutofillId();
                        try {
                            capturedPassword = (String) child.getAutofillValue().getTextValue();
                        } catch (NullPointerException n){/*do nothing, again*/}
                    }
                }
            }
        }
    }

    /**
     * Nested helper classes; store information used for autofill
     */
    class ParsedStructure {
        AutofillId userID;
        AutofillId passID;
        String URL;
    }

    class UserData{
        String userName;
        String password;
    }

    /**
     * Wrapper class that allows Requestor to begin new activities while maintaining
     * Autofill inheritance
     */
    class ActivityStarter extends Activity{
        public ActivityStarter(){ }
    }

}
