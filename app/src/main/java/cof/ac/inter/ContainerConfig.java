package cof.ac.inter;

/**
 * @author chenaijun
 *
 */

public class ContainerConfig {


	public ContainerType curContainer;
	public int container_id ;
	public int water_interval;
	public int water_capacity;
	public int material_time;
	public int rotate_speed;
	public int stir_speed;
	public WaterType water_type;
	
	public int getContainer_id() {
		return container_id;
	}
	public void setContainer(ContainerType mContainer) {
		
		curContainer = mContainer;
		if(mContainer == ContainerType.BEAN_CONTAINER){
			
			this.container_id = 0xaa;
		}else if(mContainer == ContainerType.HOTWATER_CONTAINER) {
			
			this.container_id = 0x00;
		}else if(mContainer == ContainerType.NO_ONE){
			
			this.container_id = 0x01;
		}else if(mContainer == ContainerType.NO_TOW){
			
			this.container_id = 0x02;
		}else if(mContainer == ContainerType.NO_THREE){
			
			this.container_id = 0x03;
		}else if(mContainer == ContainerType.NO_FOUR){
			
			this.container_id = 0x04;
		}else if(mContainer == ContainerType.NO_FIVE) {
			
			this.container_id = 0x05;
		}else if(mContainer == ContainerType.NO_SIX) {
			
			this.container_id = 0x06;
		}else {
			 this.container_id = 0x01;
		}
	}
	public ContainerType getContainer() {
		
		return curContainer;
	}
	
	
	public int getWater_interval() {
		return water_interval;
	}
	public void setWater_interval(int water_interval) {
		this.water_interval = water_interval;
	}
		
	public int getWater_capacity() {
		return water_capacity;
	}
	public void setWater_capacity(int water_capacity) {
		this.water_capacity = water_capacity;
	}
	public int getMaterial_time() {
		return material_time;
	}
	public void setMaterial_time(int material_time) {
		this.material_time = material_time;
	}
	public int getRotate_speed() {
		return rotate_speed;
	}
	public void setRotate_speed(int rotate_speed) {
		this.rotate_speed = rotate_speed;
	}
	public int getStir_speed() {
		return stir_speed;
	}
	public void setStir_speed(int stir_speed) {
		this.stir_speed = stir_speed;
	}
	public WaterType getWater_type() {
		return water_type;
	}
	public void setWater_type(WaterType water_type) {
		this.water_type = water_type;
	}
	
	
}
