/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses;

/**
 *
 * @author jorge
 */
public class ContainerObject {

    public String origin;
    public String body;
    public String[] destinations;

    public ContainerObject(String origin, String body, String[] destination) {
        this.origin = origin;
        this.body = body;
        this.destinations = destination;
    }
    
    
}
