/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.Pay.Pay;
import com.mycompany.User.UserDB;
import com.mycompany.Wage.Wage;

/**
 *
 * @author papadako
 */
public class ExampleAPI {

    /**
     * An example of adding a new member in the database
     *
     * @param args the command line arguments
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException{
        
        /*
        User user = new User();
        user.setUserID(3);
        user.setfullName("filip");
        user.setAddress("csd voutes");
        user.setBank("pirews");
        user.setCategory("CONTRACT TEACHER");
        user.setChildren(3);
        user.setDepartment("security");
        user.setIBAN("iban 8elw");
        user.setStartDate("27/12/2019");
        user.setStatus("MARRIED");
        user.setTelephone("6969696969");
        System.out.println("==>Adding users");
        UserDB.updateUser(user);
        */
        
        Wage wage = new Wage();
        wage.setID(3);
        wage.setStatus("MARRIED");
        wage.setYears(0);
        wage.setCategory("CONTRACT TEACHER");
        
        //WageDB.addWage(wage);
        //WageDB.promote(1);
        //System.out.println(WageDB.canDelete(1));
        //WageDB.addWage(wage);
        //WageDB.deleteWage(0);
        //System.out.println(WageDB.getWage(1).getCategory());
        //UserDB.deleteUser(1);
        
        Pay pay = new Pay();
        pay.setDate("01/01/2020");
        pay.setFullname("Filip");
        pay.setID(3);
        pay.setPay(600.0);
        //PayDB.addPay(pay);
        //System.out.println(PayDB.getPay(3).getFullname());
        //System.out.println(PayDB.updatePay(pay));
        
        //UserDB.updateCategory(6, "PERMANENT TEACHER");
        //System.out.println(UserDB.getUser(6));

        System.out.println(UserDB.PayUsers("CONTRACT TEACHER"));
        
        /*
        List<User> users = UserDB.getUsers();
        int i = 0;
        System.out.println("==>Retrieving");
        for (User userIt : users) {
            System.out.println("userIt:" + i++);
            System.out.println(userIt);
        }
        */
        // Add a wish as info

    }
}

