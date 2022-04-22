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

        //Structure from request
        List<FillContext> context = fillRequest.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();

        ParsedStructure parsedStruct = new ParsedStructure();
        Credentials userData = new Credentials("eller010", "password", " ");

        //fetching user data from AssistStructure
        parseStructure(structure, parsedStruct);
        fetchUserData(parsedStruct, userData);
        //Parcel id1 = null, id2 = null;

        RemoteViews userNamePresentation = new RemoteViews(this.getPackageName(), R.id.TextEmailAddress);
        //userNamePresentation.writeToParcel(id1, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        RemoteViews passwordPresentation = new RemoteViews(this.getPackageName(), R.id.TextPassword);
        //passwordPresentation.writeToParcel(id2, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        //parsedStruct.userID = userNamePresentation.getViewId();
        //Adds dataset with credentials to response

        FillResponse fillResponse = new FillResponse.Builder()
                .addDataset(new Dataset.Builder()
                            .setValue(parsedStruct.userID,
                                    AutofillValue.forText(userData.getUName()), userNamePresentation)
                            .setValue(parsedStruct.passID,
                                    AutofillValue.forText(userData.getPasswd()), passwordPresentation)
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
        //todo:
        for(int i = 0; i < nodes; i++){
            AssistStructure.WindowNode window = struct.getWindowNodeAt(i);
            AssistStructure.ViewNode view = window.getRootViewNode();
            traverseNode(view, parser);
        }//end for
    }

    /**
     * Helper function to parse through ViewNode information
     * @param viewNode
     * @param parser
     */
    private static void traverseNode(AssistStructure.ViewNode viewNode, ParsedStructure parser){
        if ((viewNode.getAutofillHints() != null) && (viewNode.getAutofillHints().length > 0)){
            //todo:
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
