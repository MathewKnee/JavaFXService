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
public class Client extends User{
    Client(int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.type = 2;
    }
    @Override
    public int[] getPermissions() {
        int ret[] = new int[7];
        ret[0]=1;
        ret[1]=0;
        ret[2]=1;
        ret[3]=0;
        ret[4]=0;
        ret[5]=0;
        ret[6]=0;
        return ret;
    }
    
}
