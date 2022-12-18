#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.reflect.TypeToken;
import com.numob.appbaseandroid.Controller.NULoginAccountController;
import com.numob.appbaseandroid.Model.NUDevice;
import com.numob.appbaseandroid.Utility.NULocalBroadcast;
import com.numob.appbaseandroid.Utility.StringUtils;
import com.softzoo.inventory.App;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

#parse("File Header.java")
public class ${NAME} extends BaseData implements Serializable {

    private static final Type TYPE_ARRAY_LIST = new TypeToken<ArrayList<${NAME}>>() {}.getType();
    private static final String TABLE_NAME = "${TABLE_NAME}";

    public String identifier;
    public String databaseTable = TABLE_NAME;
    public String ownerIdentifier;
    public String deviceIdentifier;
    public int serverChangeTag;
    public int localChange;
    public int orgCode;
    @Nullable
    public Integer deleted;

    public String params;
    public long createMS;
    public long updateMS;


    public ${NAME}() {
        this.identifier = StringUtils.getUUID();
        this.databaseTable = TABLE_NAME;
        this.ownerIdentifier = NULoginAccountController.currentAccountIdentifier();
        this.deviceIdentifier = NUDevice.identifier;
        this.serverChangeTag = 0;
        this.localChange = 1;
        this.orgCode = 0;
        this.deleted = 0;
        this.params = "";
        Date current = new Date();
        this.createMS = current.getTime();
        this.updateMS = current.getTime();
    }


    @Override
    public boolean delete() {
        if (super.delete()) {
            sendChangedBroadcast();
            return true;
        }
        return false;
    }

    @Override
    public boolean save() {
        if (super.save()){
            sendChangedBroadcast();
            return true;
        }
        return false;
    }

    private static void sendChangedBroadcast() {
        sendChangedBroadcast(new Date());
    }

    private static void sendChangedBroadcast(Date date) {
        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(getDownloadedAction()).putExtra("time", date.getTime()));
    }

    public static String getDownloadedAction() {
        return NULocalBroadcast.tableDidDownloadNotification(TABLE_NAME.toLowerCase());
    }
}
