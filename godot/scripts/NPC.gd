extends Character
class_name NPC

var tile_size = 5

var npc_think_time = 0.4
var npc_think_timer = null

# todo deadlock timer for navigation

export var npc_type = "retired_person"

var targets = []
var current_target : RigidBody2D = null

var result_up
var result_right
var result_down
var result_left

func _ready():
	_setup_npc_think_time_timer()
	_setup_npc_type()
	add_to_group("npcs")


func _on_collision(colliding_body):
	#print("npc collision " + colliding_body.name)
	pass 

func _setup_npc_type():
	match npc_type:
		"retired_person":
			speed = 90
			strength = 5
			
		"yolo_hipster":
			speed = 120
			strength = 10
		"family_mum":
			speed = 150
			strength = 30
			npc_think_time = 0.25


func _add_target(newtarget):
	#print("adding " + newtarget.name)
	targets.append(newtarget)
	_find_closest_target()

func _find_closest_target():
	if targets.size() == 0:
		print("no targets!")
		pass
		
	var closest_distance = targets[0].global_position - self.global_position
	var closest_target = targets[0]
	for target in targets:
		var distance = target.global_position - self.global_position
		if (distance < closest_distance):
			closest_target = target
			closest_distance = distance
	
	current_target = closest_target
	#print("new target: " + current_target.name)

func _move():
	var target_direction = _get_direction_to_target()
	#var direction_to_move = _calculate_route(target_direction)
	#var movement_direction = direction_to_move.normalized() * tile_size * speed
	var movement_direction = target_direction.normalized() * tile_size *speed
	#print(movement_direction)
	move_and_slide(movement_direction)
	
	
func _get_direction_to_target():
	if current_target != null:
		return current_target.global_position - global_position
	else:
		print("target is null!")
		return Vector2(1,1)

func _setup_npc_think_time_timer():
	npc_think_timer = Timer.new()
	npc_think_timer.set_one_shot(true)
	npc_think_timer.set_wait_time(npc_think_time)
	npc_think_timer.connect("timeout", self, "_on_think_timer_timeout")
	add_child(npc_think_timer)
	npc_think_timer.start()
	#print("npc timer set up")

func _on_think_timer_timeout():
	#print("npc timeout")
	if current_target != null:
		_move()
		npc_think_timer.start()
	else:
		print("target is null")
	
#func _physics_process(delta):
#	var space_state = $CollisionShape2D.get_world_2d().direct_space_state


func _calculate_route(targetdirection):
	if (result_up == null || result_down == null || result_right == null ||result_left == null):
		return Vector2(0, 0)
	return targetdirection
