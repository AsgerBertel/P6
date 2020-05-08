package PerformanceTester;

import java.util.Objects;

public class QueriedView {
    String viewName;
    int amount_of_queries = 0;

    public QueriedView(String viewName) {
        this.viewName = viewName;
    }

    public void setAmount_of_queries(int amount_of_queries) {
        this.amount_of_queries += amount_of_queries;
    }

    public String getViewName() {
        return viewName;
    }

    public int getAmount_of_queries() {
        return amount_of_queries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueriedView that = (QueriedView) o;
        return Objects.equals(viewName, that.viewName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewName);
    }
}
