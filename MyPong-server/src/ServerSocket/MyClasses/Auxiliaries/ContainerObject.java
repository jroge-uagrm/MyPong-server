/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Auxiliaries;

/**
 *
 * @author jorge
 */
public class ContainerObject{

    public String origin;
    public Object body;
    public String[] destinations;

    public ContainerObject(String origin, Object body, String[] destination) {
        this.origin = origin;
        this.body = body;
        this.destinations = destination;
    }
    
    
}
