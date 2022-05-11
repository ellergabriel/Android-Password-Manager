package com.example.knox.systemComponents;
import java.security.GeneralSecurityException;
import java.util.List;

import android.app.Activity;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.CancellationSignal;
import android.os.Handler;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.util.Pair;
import android.view.ViewStructure;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import com.example.knox.R;
import com.scottyab.aescrypt.AESCrypt;

public final class Requestor extends AutofillService {

    //Singleton creation pattern
    private static volatile Requestor instance = null;
    private static volatile long timer = -1;

    //feel free to change, just testing the cipher key
    private static final String key = "to be fair, you have to have a very high IQ to understand Rick and Morty. " +
            "The humour is extremely subtle, and without a solid grasp of theoretical physics most " +
            "of the jokes will go over a typical viewer's head. There's also Rick's nihilistic " +
            "outlook, which is deftly woven into his characterisation- his personal philosophy " +
            "draws heavily from Narodnaya Volya literature, for instance. The fans understand this " +
            "stuff; they have the intellectual capacity to truly appreciate the depths of these jokes, " +
            "to realise that they're not just funny- they say something deep about LIFE. As a " +
            "consequence people who dislike Rick & Morty truly ARE idiots- of course they wouldn't " +
            "appreciate, for instance, the humour in Rick's existential catchphrase \"Wubba Lubba Dub Dub,\" " +
            "which itself is a cryptic reference to Turgenev's Russian epic Fathers and Sons. " +
            "I'm smirking right now just imagining one of those addlepated simpletons scratching " +
            "their heads in confusion as Dan Harmon's genius wit unfolds itself on their television " +
            "screens. What fools.. how I pity them.";
    private static String capturedUName;
    private static String capturedPassword;
    public static String capturedURL;
    public Requestor(){} //constructor must be public to implement autofill service

    @Override
    public void onFillRequest(@NonNull FillRequest fillRequest, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback fillCallback) {
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
        String dummy = "............";

        if(!cred.getPasswd().equals("DNE")){
            cred.setPasswd(decrypt(cred.getPasswd()));
            if(!Validator.isSessionValid()) {
                //Validator.getInstance().createPrompt(getApplicationContext().getApplicationContext());
            }//checks for 60 second login window
        }

        userNamePresentation.setTextViewText(android.R.id.text1, cred.getUName());
        passwordPresentation.setTextViewText(android.R.id.text1, dummy);
        //Adds dataset with credentials to response
        if(parsedStruct.userID == null || parsedStruct.passID == null){
            fillCallback.onFailure("failed to find autofill fields");
            return;
        }

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
                ViewStructure.HtmlInfo info = child.getHtmlInfo();
                if(info != null){
                    System.out.println(info.getAttributes().toString());
                }
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
                    searchHTML(info);
                    //text field has some hint, check for id
                    if( (child.getHint().equals("Username") || searchHTML(info) == 1)
                        && child.getAutofillHints() != null){
                        parser.userID = child.getAutofillId();
                        try {
                            //only for save requests; fill requests will always have the null pointer
                            capturedUName = (String) child.getAutofillValue().getTextValue();
                        } catch (NullPointerException n) { /*do nothing*/}
                    } else if ( (child.getHint().equals("Password")|| searchHTML(info) == 2)
                                && child.getAutofillHints() != null){
                        parser.passID = child.getAutofillId();
                        try {
                            //same as other try/catch
                            capturedPassword = (String) child.getAutofillValue().getTextValue();
                            capturedPassword = encrypt(capturedPassword);
                        } catch (NullPointerException n){
                            /*do nothing, again*/
                        }
                    }
                }
            }
        }
    }

    /**
     * Function searchs htmlinfo object generated by AssistStructure, manually searching for
     * login field labels
     * @param h - htmlinfo received from AssistStructure
     * @return 0 if no labels are found, 1 for username label, 2 for password label
     */
    public static int searchHTML(ViewStructure.HtmlInfo h){
        if(h == null){
            return 0;
        }
        List<Pair<String, String>> att = h.getAttributes();
        //htmlInfo[2] should always be the label header, change if not found
        String labels = att.get(2).second.toLowerCase();
        for(int i = 0; i < att.size(); i++){
            if(labels.contains("username") || labels.contains("email")){
                return 1;
            } else if (labels.contains("password")){
                return 2;
            }
        }
        return 0;
    }



    /**
     * Helper function that encapsulates AESCrypt encrypt function
     * @param pass - password to be encrypted
     * @return - encrypted password if string is valid, empty string otherwise
     */
    public static String encrypt(String pass){
        try{
            return AESCrypt.encrypt(key, pass);
        } catch (GeneralSecurityException g){
            return "";
        }
    }

    public static String decrypt(String hash){
        try{
            return AESCrypt.decrypt(key, hash);
        } catch (GeneralSecurityException g){
            return "";
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
        public ActivityStarter(){

        }
    }

}
