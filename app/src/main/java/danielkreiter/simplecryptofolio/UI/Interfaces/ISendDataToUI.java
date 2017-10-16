package danielkreiter.simplecryptofolio.UI.Interfaces;

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
