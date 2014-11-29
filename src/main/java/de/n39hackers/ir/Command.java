package de.n39hackers.ir;

import java.util.List;

/**
 * Created by n39hackers on 28/11/14.
 *
 * See REPL's documentation for Command's purpose.
 *
 */
public interface Command {
    public void run(QueryIndexList queryIndexList, List<ReutersArticle> articles);
    public String getName();
}
