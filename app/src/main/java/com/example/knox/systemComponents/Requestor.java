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
import android.service.autofill.SaveRequest;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;


import androidx.annotation.NonNull;

import com.example.knox.R;

public final class Requestor extends AutofillService {

    //Singleton creation pattern
    private static volatile Requestor instance = null;
    private static volatile Timer timer = null;
    public Requestor(){} //constructor must be public to implement autofill service

    @Override
    public void onFillRequest(@NonNull FillRequest fillRequest, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback fillCallback) {
        /*todo:check if session timer has ran out
        if timer is out, show fingerprint prompt to user before continuing
        else, keep it going*/

        //Structure from request
        List<FillContext> context = fillRequest.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();

        ParsedStructure parsedStruct = new ParsedStructure();
        //Credentials userData = new Credentials("eller010", "password", " ");

        //fetching user data from AssistStructure
        parseStructure(structure, parsedStruct);
        //fetchUserData(parsedStruct, userData);
        //Parcel id1 = null, id2 = null;

        RemoteViews userNamePresentation = new RemoteViews(this.getPackageName(), android.R.layout.simple_list_item_1);
        //todo: hardcoded credentials for testing purposes only
        userNamePresentation.setTextViewText(android.R.id.text1,"eller010");
        RemoteViews passwordPresentation = new RemoteViews(this.getPackageName(), android.R.layout.simple_list_item_1);
        passwordPresentation.setTextViewText(android.R.id.text1, "password");
        //Adds dataset with credentials to response

        //.todo: more hardcoded credentials; change after database is implemented
        FillResponse fillResponse = new FillResponse.Builder()
                .addDataset(new Dataset.Builder()
                            .setValue(parsedStruct.userID,
                                    AutofillValue.forText("eller010"), userNamePresentation)
                            .setValue(parsedStruct.passID,
                                    AutofillValue.forText("password"), passwordPresentation)
                            .build())
                .build();

        fillCallback.onSuccess(fillResponse);


    }

    //todo: once database is implemented, make onSaveRequest encrypt and save to the database
    @Override
    public void onSaveRequest(@NonNull SaveRequest saveRequest, @NonNull SaveCallback saveCallback) {

    }

    /**
     * Lazy Singleton constructor
     * @return single instance of Requestor
     */
    public static Requestor getInstance(){
        if(instance == null){
            instance = new Requestor();
            timer = new Timer();
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
     * Helper function to parse through ViewNode information via Children field
     * @param viewNode
     * @param parser
     */
    private static void traverseNode(AssistStructure.ViewNode viewNode, ParsedStructure parser){
        if (viewNode.getChildCount() > 0){
            //todo:
            AssistStructure.ViewNode child;
            for(int i = 0; i < viewNode.getChildCount(); i++){
                child = viewNode.getChildAt(i);
                String tester = child.getHint();
                if(child.getChildCount() > 0){
                    traverseNode(child, parser);//recursive call for now;
                    //todo: implement stack when autofill is fully working
                } else if(!(child.getAutofillHints() == null) || !(tester == null)){
                    //text field has some hint, check for id
                    if(child.getHint().equals("Username")){
                        parser.userID = child.getAutofillId();
                    } else if (child.getHint().equals("Password")){
                        parser.passID = child.getAutofillId();
                    }
                }
            }
        }
    }

    /**
     * Takes in already filled ParsedStructure with userID and passID and assigns to UserData object
     * @param parser
     * @param data
     */
    private static void fetchUserData(ParsedStructure parser, Credentials data){
        //todo
    }

    /**
     * Nested helper classes; store information used for autofill
     */
    class ParsedStructure {
        AutofillId userID;
        AutofillId passID;
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
