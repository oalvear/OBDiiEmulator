/**
 * 
 */
package es.upv.grc.obdemulator.engine;

import es.upv.grc.obdemulator.elm.ECUVehicle;

/**
 * @author Oscar Alvear
 * 
 */
public abstract class OBDGenerator implements Runnable {

	ECUVehicle data;

        boolean running;
        
	public abstract void initData();
        
        public void setData(ECUVehicle data) {
            this.data = data;
        }
        public ECUVehicle getData() {
            return data;
        }
        public void terminate() {
            this.running = false;
        }
}
