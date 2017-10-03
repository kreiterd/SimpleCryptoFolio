package danielkreiter.simplecryptofolio.UI.Activity;

import org.json.JSONObject;

public interface ISendDataToActivity {

    void postExecuteUpdateView(JSONObject result);

    void preExecuteUpdateView();
}
