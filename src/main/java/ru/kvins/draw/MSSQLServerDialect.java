/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kvins.draw; 
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.dialect.SQLServerDialect;
/**
 *
 * @author Эдуард
 */
public class MSSQLServerDialect  extends SQLServerDialect {
  public MSSQLServerDialect() {
  super();
   
  registerColumnType(Types.VARCHAR, "nvarchar($l)");
  registerColumnType(Types.BIGINT, "bigint");
  registerColumnType(Types.BIT, "bit");
 }
}
