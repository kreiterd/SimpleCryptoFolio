package danielkreiter.simplecryptofolio.UI;

import org.json.JSONObject;

/**
 */
public interface ISendDataToUI {

    /**
     * @param result the result
     */
    void postExecuteUpdateView(JSONObject result);

    /**
     * Pre execute update view.
     */
    void preExecuteUpdateView();
}
