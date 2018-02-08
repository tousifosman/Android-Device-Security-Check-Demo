package utils;

/**
 * Created by osman.tousif on 2/8/2018.
 */

public interface ResultListener {
    void onSuccess();
    void onFailure();
    void onError();
}
