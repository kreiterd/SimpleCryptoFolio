package danielkreiter.simplecryptofolio.UI.Interfaces;

import org.json.JSONObject;

import danielkreiter.simplecryptofolio.UI.Tasks.AsyncTaskResult;

/**
 */
public interface ISendDataToUI {

    /**
     * @param result the result
     */
    void postExecuteUpdateView(AsyncTaskResult<JSONObject> result);

    /**
     * Pre execute update view.
     */
    void preExecuteUpdateView();
}
