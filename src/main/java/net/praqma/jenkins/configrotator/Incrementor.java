/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.praqma.jenkins.configrotator;

/**
 *
 * @author Mads
 */
public enum Incrementor {
    NEXT,FIXED,NEWEST;

    @Override
    public String toString() {
        switch(this) {
            case FIXED:
                return "Fixed";
            case NEXT:
                return "Next";
            case NEWEST:
                return "Newest";
            default: 
                return null;
        } 
    }
    
    
}
