package danielkreiter.simplecryptofolio.UI;

import org.json.JSONObject;

public interface ISendDataToActivity {

    void postExecuteUpdateView(JSONObject result);

    void preExecuteUpdateView();
}
