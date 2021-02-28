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
public abstract class User {
    protected int type;
    protected int id;
    protected String name;
    protected String surname;
    public abstract int[] getPermissions();
}
