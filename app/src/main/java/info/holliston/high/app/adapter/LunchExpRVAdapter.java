package info.holliston.high.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.holliston.high.app.MainActivity;
import info.holliston.high.app.R;
import info.holliston.high.app.datamodel.Article;

/**
 * A class to manage Lunch data in an expandable RecyclerView
 *
 * @author Tom Reeve
 */

public class LunchExpRVAdapter extends ExpandableRVAdapter {

    public LunchExpRVAdapter(MainActivity ma){
        super(ma, R.layout.rv_row_lunch, true);
    }

    /**
     * Adds data to the data row viewholder
     *
     * @param va           the viewholder containing the data
     * @param article      the article of data to be shown
     */
    @Override
    protected void setDataRow(RecyclerView.ViewHolder va, Article article, int position) {
        ExpandableRVAdapter.ViewHolderArticleExpandable vha =
                (ExpandableRVAdapter.ViewHolderArticleExpandable) va;
        vha.updateItem(position);

        SimpleDateFormat dfDay = new SimpleDateFormat("EEEE", Locale.US);
        String dayString = dfDay.format(article.getDate());

        String title = article.getTitle();
        String details = article.getDetails();
        details = Html.fromHtml(details).toString();

        vha.text1.setText(dayString);
        vha.text2.setText(title);
        vha.detailsView.setText(details);
    }

    /**
     * Identifies row headers, and inserts the headers into the list of items. In this case,
     * data will be grouped by week of the year (this week, next week, etc)
     *
     * @param list    the data to be sorted, in List form
     * @return        a list of headers and data rows
     */
    protected List<Object> sortIntoGroups(List<Article> list) {

        //tracks the current group
        int currentWeek = -1;
        List<Object> tempList = new ArrayList<>();

        // if isTrimmable and it's after 2pm and the data is today's data, skip the first row
        if (isTrimmable && needsTrimming(list)) {
            list.remove(0);
        }

        //cycle through the articles
        for (Article article : list) {

            //get the article's calendar info
            Date schedDate = article.getDate();
            Calendar schedCal = Calendar.getInstance();
            schedCal.setTime(schedDate);
            int artWeek = schedCal.get(Calendar.WEEK_OF_YEAR);

            // if the article's week is not the current group's week, it's time for a new group
            if (currentWeek != artWeek) {
                Calendar todaycal = Calendar.getInstance();
                todaycal.setTime(new Date());
                int thisWeek = todaycal.get(Calendar.WEEK_OF_YEAR);

                String headerString = "";
                if (artWeek == thisWeek) {
                    headerString = getContext().getString(R.string.this_week);
                } else if ((artWeek == thisWeek+1) || (artWeek == 1)) {
                    headerString = getContext().getString(R.string.next_week);
                }
                tempList.add(headerString);
                currentWeek = artWeek;
            }
            tempList.add(article);
        }
        return tempList;
    }
}

