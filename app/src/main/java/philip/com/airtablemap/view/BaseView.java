package philip.com.airtablemap.view;

/**
 * Base view interface for view
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    void onError(String message);
}
