/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mathew
 */
public class Service {

    private final String db_address;
    private final String root_user;
    private final String root_passwd;
    public User curr_user;

    public Service(String db_address, String root_user, String root_passwd) {
        this.db_address = db_address;
        this.root_user = root_user;
        this.root_passwd = root_passwd;
        this.curr_user = null;
    }

    public int login(String user, String passwd) {
        int ret = -2;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("SELECT type,id,name,surname FROM users WHERE username = ? AND passwd = ?");
            stmt.setString(1, user);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int type = rs.getInt("type");
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                switch (type) {
                    case 0:
                        curr_user = new Admin(id, name, surname);
                        ret = 0;
                        break;
                    case 1:
                        curr_user = new Employee(id, name, surname);
                        ret = 1;
                        break;
                    case 2:
                        curr_user = new Client(id, name, surname);
                        ret = 2;
                        break;
                    default:
                        break;
                }
            } else {
                ret = -1;
            }
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            ret = -2;
        }
        return ret;
    }

    public boolean addOrder(String device_name, int client_id, int state, String desc) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO order_queue (device,client_id,state,description) VALUES (?,?,?,?)");
            stmt.setString(1, device_name);
            stmt.setInt(2, client_id);
            stmt.setInt(3, state);
            stmt.setString(4, desc);
            stmt.executeUpdate();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeOrder(int order_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM order_queue WHERE ID = ?");
            stmt.setInt(1, order_id);
            stmt.executeUpdate();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean markAsFinished(int order_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("UPDATE order_queue SET state = 2 WHERE ID = ?");
            stmt.setInt(1, order_id);
            stmt.executeUpdate();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<ArrayList<String>> showOrders() {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        int perms[] = curr_user.getPermissions();
        if (perms[2] == 1) {
            switch (curr_user.type) {
                case 0: {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                        PreparedStatement stmt = con.prepareStatement("SELECT * FROM order_queue");

                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            ArrayList<String> row = new ArrayList<>();
                            row.add(Integer.toString(rs.getInt("ID")));
                            row.add(rs.getString("device"));
                            row.add(Integer.toString(rs.getInt("client_id")));
                            row.add(rs.getString("placed"));
                            int state = rs.getInt("state");
                            switch (state) {
                                case 0:
                                    row.add("waiting");
                                    break;
                                case 1:
                                    row.add("taken");
                                    break;
                                case 2:
                                    row.add("finished");
                                    break;
                                default:
                                    break;
                            }
                            row.add(rs.getString("description"));
                            ret.add(row);
                        }
                        con.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        ArrayList<String> row = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            row.add("ERROR");
                        }
                        ret.add(row);
                    }
                    break;
                }
                case 1: {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                        PreparedStatement stmt = con.prepareStatement("SELECT * FROM order_queue WHERE state = 0");

                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            ArrayList<String> row = new ArrayList<>();
                            row.add(Integer.toString(rs.getInt("ID")));
                            row.add(rs.getString("device"));
                            row.add(rs.getString("placed"));
                            int state = rs.getInt("state");
                            switch (state) {
                                case 0:
                                    row.add("waiting");
                                    break;
                                case 1:
                                    row.add("taken");
                                    break;
                                case 2:
                                    row.add("finished");
                                    break;
                                default:
                                    break;
                            }
                            row.add(rs.getString("description"));
                            ret.add(row);
                        }
                        con.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        ArrayList<String> row = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            row.add("ERROR");
                        }
                        ret.add(row);
                    }
                    break;
                }
                case 2: {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                        PreparedStatement stmt = con.prepareStatement("SELECT * FROM order_queue WHERE client_id = ?");
                        stmt.setInt(1, curr_user.id);
                        ResultSet rs = stmt.executeQuery();
                        while (rs.next()) {
                            ArrayList<String> row = new ArrayList<>();
                            row.add(rs.getString("device"));
                            row.add(rs.getString("placed"));
                            int state = rs.getInt("state");
                            switch (state) {
                                case 0:
                                    row.add("waiting");
                                    break;
                                case 1:
                                    row.add("taken");
                                    break;
                                case 2:
                                    row.add("finished");
                                    break;
                                default:
                                    break;
                            }
                            row.add(rs.getString("description"));
                            ret.add(row);
                        }
                        con.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        ArrayList<String> row = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            row.add("ERROR");
                        }
                        ret.add(row);
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return ret;
    }

    public boolean addUser(String username, String passwd, int type, String name, String surname) {
        if ((curr_user.getPermissions())[3] == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO users (username,passwd,type,name,surname) VALUES (?,?,?,?,?)");
                stmt.setString(1, username);
                stmt.setString(2, passwd);
                stmt.setInt(3, type);
                stmt.setString(4, name);
                stmt.setString(5, surname);
                stmt.executeUpdate();
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean removeUser(int user_id) {
        if (curr_user.getPermissions()[4] == 1) {
            if (curr_user.id != user_id) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                    PreparedStatement stmt = con.prepareStatement("DELETE FROM users WHERE ID = ?");
                    stmt.setInt(1, user_id);
                    stmt.executeUpdate();
                    con.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ArrayList<String>> showUsers() {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        if (curr_user.getPermissions()[5] == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(Integer.toString(rs.getInt("ID")));
                    row.add(rs.getString("username"));
                    row.add("*******");

                    int type = rs.getInt("type");
                    switch (type) {
                        case 0:
                            row.add("admin");
                            break;
                        case 1:
                            row.add("employee");
                            break;
                        case 2:
                            row.add("client");
                            break;
                        default:
                            break;
                    }
                    row.add(rs.getString("name"));
                    row.add(rs.getString("surname"));
                    ret.add(row);
                }
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    row.add("ERROR");
                }
                ret.add(row);
            }
        } else {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                row.add("ERROR");
            }
            ret.add(row);
        }
        return ret;
    }

    public boolean assignOrder(int emp_id, int order_id) {
        if ((curr_user.getPermissions())[1] == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO assigned_orders (employee_ID,order_ID) VALUES (?,?)");
                stmt.setInt(1, emp_id);
                stmt.setInt(2, order_id);
                stmt.executeUpdate();
                PreparedStatement stmt1 = con.prepareStatement("UPDATE order_queue SET state = 1 WHERE ID = ?");
                stmt1.setInt(1, order_id);
                stmt1.executeUpdate();
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ArrayList<String>> takenOrders() {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        if (curr_user.type == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("SELECT ID, device, placed, description FROM order_queue INNER JOIN assigned_orders ON order_queue.ID = assigned_orders.order_ID WHERE assigned_orders.employee_ID = ? AND order_queue.state = 1");
                stmt.setInt(1, curr_user.id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(Integer.toString(rs.getInt("ID")));
                    row.add(rs.getString("device"));
                    row.add(rs.getString("placed"));
                    row.add(rs.getString("description"));
                    ret.add(row);
                }
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    row.add("ERROR");
                }
                ret.add(row);
            }
        } else {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                row.add("ERROR");
            }
            ret.add(row);
        }

        return ret;
    }

    public double showPerformanceRate() {
        double ret;
        int n_emp, n_ord;
        if (curr_user.type == 0 || curr_user.type == 2) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM users WHERE type = 1");
                ResultSet rs = stmt.executeQuery();
                rs.next();
                n_emp = rs.getInt(1);
                PreparedStatement stmt1 = con.prepareStatement("SELECT count(*) FROM order_queue WHERE NOT state = 2");
                ResultSet rs1 = stmt1.executeQuery();
                rs1.next();
                n_ord = rs1.getInt(1);
                ret = ((double) n_ord) / ((double) n_emp);
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                ret = 0.0;
            }
        } else {
            ret = 0.0;
        }
        return ret;
    }

    public ArrayList<ArrayList<String>> getDelayedOrders() {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        if (curr_user.getPermissions()[6] == 1) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM order_queue WHERE DATEDIFF(CURRENT_TIMESTAMP,placed) >= 5");

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(Integer.toString(rs.getInt("ID")));
                    row.add(rs.getString("device"));
                    row.add(Integer.toString(rs.getInt("client_id")));
                    row.add(rs.getString("placed"));
                    int state = rs.getInt("state");
                    switch (state) {
                        case 0:
                            row.add("waiting");
                            break;
                        case 1:
                            row.add("taken");
                            break;
                        case 2:
                            row.add("finished");
                            break;
                        default:
                            break;
                    }
                    row.add(rs.getString("description"));
                    ret.add(row);
                }
                con.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    row.add("ERROR");
                }
                ret.add(row);
            }
        } else {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                row.add("ERROR");
            }
            ret.add(row);
        }

        return ret;
    }
    
    public boolean employeeExists(int user_id){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(ID) FROM users WHERE type = 1 and ID = ?");
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int n_emp = rs.getInt(1);
            con.close();
            if(n_emp == 1){
                return true;
            }else{
                return false;
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        
    }
    public ArrayList<Integer> getYears(){
        ArrayList<Integer> ret = new ArrayList<>();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("SELECT YEAR(placed) FROM `order_queue` GROUP BY YEAR(placed) DESC");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ret.add(rs.getInt(1));
            }
        }catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        
        return ret;
    }
    public HashMap<String,Integer> getOrdersByMonth(int year){
        HashMap<String,Integer> ret = new HashMap<>();
        String[] months = {"January", "February","March","April","May","June","July","August","September","October","November","December"};
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("SELECT MONTH(placed),COUNT(ID) FROM `order_queue` WHERE YEAR(placed) = ? GROUP BY MONTH(placed)");
            stmt.setInt(1,year);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int month = rs.getInt(1);
                int num = rs. getInt(2);
                ret.put(months[month-1],num);
            }
        }catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        for(int i = 0;i<=11;i++){
            if(!ret.containsKey(months[i])){
                ret.put(months[i],0);
            }
        }
        return ret;
    }
    
    public HashMap<String,Integer> getUserDist(){
        HashMap<String,Integer> ret = new HashMap<>();
        String[] users = {"Admin","Employee","Client"};
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(db_address, root_user, root_passwd);
            PreparedStatement stmt = con.prepareStatement("SELECT type, COUNT(ID) FROM `users` GROUP BY type");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int type = rs.getInt(1);
                int num = rs. getInt(2);
                ret.put(users[type],num);
            }
        }catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        for(int i = 0;i<3;i++){
            if(!ret.containsKey(users[i])){
                ret.put(users[i],0);
            }
        }
        return ret;
    }
}
