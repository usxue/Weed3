package noear.weed;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 14/10/30.
 */
public class DbTranQueue {
    private List<DbTran> queue = new ArrayList<DbTran>();

    public Object result;//用于存放中间结果

    protected void add(DbTran tran)
    {
        queue.add(tran);
    }

    private void commit()  throws SQLException
    {
        for (DbTran tran : queue) //从头到尾提交
            tran.commit(true);
    }

    //isQueue:是否由Queue调用的
    protected void rollback(boolean isQueue)  throws SQLException {
        doRollback();

        if(isQueue==false)
            close();
    }

    private void doRollback() //从尾向头回滚
    {
        int len = queue.size();
        for (int i = len - 1; i > -1; i--) {
            DbTran tran = queue.get(i);

            try {
                tran.rollback(true);
            } catch (Exception ex) {
            }
        }
    }

    private void close() throws SQLException
    {
        for (DbTran tran : queue) //从头到关闭（关闭时，不能影响其它事务）
        {
            try {
                tran.close(true);
            }catch (Exception ex) {
                WeedConfig.logException(null, ex);
            }
        }
    }

    /*结束事务
    * */
    public void complete() throws SQLException {
        try {
            commit();
        } catch (SQLException ex) {
            rollback(true);
            throw ex;
        } finally {
            close();
        }
    }

}
