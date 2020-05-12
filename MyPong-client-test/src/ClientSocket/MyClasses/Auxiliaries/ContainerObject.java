/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.MyClasses.Auxiliaries;

/**
 *
 * @author jorge
 */
public class ContainerObject{

    public String origin;
    public Object body;
    public String[] destinations;

    public ContainerObject(String origin, Object body, String[] destinations) {
        this.origin = origin;
        this.body = body;
        this.destinations = destinations;
    }
}
