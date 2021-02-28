/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxservice;

/**
 *
 * @author Mathew
 */
public class Admin extends User{
    Admin(int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.type = 0;
    }
    @Override
    public int[] getPermissions() {
        int ret[] = new int[7];
        for(int i=0;i<7;i++) ret[i] = 1;
        return ret;
    }
    
}
