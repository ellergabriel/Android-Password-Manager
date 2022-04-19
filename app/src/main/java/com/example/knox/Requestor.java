package com.example.knox;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.os.CancellationSignal;
import android.provider.Settings;
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

public final class Requestor extends AutofillService {

    //Singleton creation pattern
    private static volatile Requestor instance = null;
    private static volatile Timer timer = null;
    private volatile AutofillManager manager;
    public Requestor(){} //constructor must be public to implement autofill service

    @Override
    public void onFillRequest(@NonNull FillRequest fillRequest, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback fillCallback) {
        System.out.println("autofill shazbot\n");
        manager = getSystemService(AutofillManager.class);
        if(!manager.hasEnabledAutofillServices()){
            //device does not support autofill; edge case to fill out later
            if(!manager.isAutofillSupported()){
                System.out.println("shazbot\n");
                /**
                 * no autofill on device; likely exit app or redirect to database for storage/creatoin
                 */
            } else {
                //prompts user to enable autofill from settings
                System.out.println("sad shazbot\n");
                ActivityStarter starter = new ActivityStarter();
                starter.startActivity(new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE));
            }
        }
        //Structure from request
        List<FillContext> context = fillRequest.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();

        ParsedStructure parsedStruct = new ParsedStructure();
        UserData userData = new UserData();

        //fetching user data from AssistStructure
        parseStructure(structure, parsedStruct);
        fetchUserData(parsedStruct, userData);

        RemoteViews userNamePresentation = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
        userNamePresentation.setTextViewText(android.R.id.text1, "dummy username");
        RemoteViews passwordPresentation = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
        passwordPresentation.setTextViewText(android.R.id.text1, "dummy password");

        //Adds dataset with credentials to response
        FillResponse fillResponse = new FillResponse.Builder()
                .addDataset(new Dataset.Builder()
                            .setValue(parsedStruct.userID,
                                    AutofillValue.forText(userData.userName), userNamePresentation)
                            .setValue(parsedStruct.passID,
                                    AutofillValue.forText(userData.password), passwordPresentation)
                            .build())
                .build();

        fillCallback.onSuccess(fillResponse);
    }

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
        for(int i = 0; i < nodes; i++){
            AssistStructure.WindowNode window = struct.getWindowNodeAt(i);
            AssistStructure.ViewNode view = window.getRootViewNode();
            traverseNode(view, parser);
        }//end for
    }

    /**
     *
     * @param viewNode
     * @param parser
     */
    private static void traverseNode(AssistStructure.ViewNode viewNode, ParsedStructure parser){
        if ((viewNode.getAutofillHints() != null) && (viewNode.getAutofillHints().length > 0)){
            System.out.println("shazbot has hints\n");
            //holder for now
        }
    }

    /**
     * Takes in already filled ParsedStructure with userID and passID and assigns to UserData object
     * @param parser
     * @param data
     */
    private static void fetchUserData(ParsedStructure parser, UserData data){
        data.userName = parser.userID.toString();
        data.password = parser.passID.toString();
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
        public ActivityStarter(){

        }
    }

}
