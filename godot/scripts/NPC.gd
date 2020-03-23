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
			speed = 2
			strength = 5
			
		"yolo_hipster":
			speed = 4
			strength = 10
		"family_mum":
			speed = 6
			strength = 30
			npc_think_time = 0.25


func _add_target(newtarget):
	#print("adding " + newtarget.name)
	targets.append(newtarget)
	_find_closest_target()
	_move()

func _find_closest_target():
	if targets.size() == 0:
		print("no targets!")
		pass
		
	var closest_distance = targets[0].global_position.distance_to(self.global_position)
	var closest_target = targets[0]
	for target in targets:
		var distance = target.global_position.distance_to(self.global_position)
		if (distance < closest_distance):
			closest_target = target
			closest_distance = distance
	
	current_target = closest_target
	#print("new target: " + current_target.name)

func _move():
	var target_direction = _get_direction_to_target()
	var movement_direction = target_direction.normalized() * tile_size *speed
	print(movement_direction)
	move_and_slide(movement_direction)
	
	
func _get_direction_to_target():
	if current_target != null:
		return current_target.global_position - global_position
	else:
		return Vector2.ZERO

func _setup_npc_think_time_timer():
	npc_think_timer = Timer.new()
	npc_think_timer.set_one_shot(true)
	npc_think_timer.set_wait_time(npc_think_time)
	npc_think_timer.connect("timeout", self, "_on_think_timer_timeout")
	add_child(npc_think_timer)
	npc_think_timer.start()
	#print("npc timer set up")

func _on_think_timer_timeout():
	pass
	#print("npc timeout")
	#if current_target != null:
	#	_move()
	#	npc_think_timer.start()
	#else:
	#	print("target is null")
	
func _physics_process(delta):
	#var movement_offset = _get_direction_to_target().normalized()
	#var space_state = $CollisionShape2D.get_world_2d().direct_space_state
	#var result_movement_dir = space_state.intersect_ray(Vector2(0, 0), Vector2(movement_offset.x, movement_offset.y))
	
	#if (result_movement_dir):
		# 45 cw
	#	var right = movement_offset.rotated(0.78)
		# 45 ccw
	#	var left = movement_offset.rotated(5.49)
	#	var result_right = space_state.intersect_ray(self.global_position, Vector2(right.x * 2, right.y * 2))
	#	if (!result_right):
	#		move_and_slide(right)
	#	else:
	#		var result_left = space_state.intersect_ray(self.global_position, Vector2(left.x * 2, left.y * 2))
	#		if (!result_left):
	#			move_and_slide(left)
	#		else:
	#			print("NO WAY OUT!!")
	_move()
	

func _calculate_route(targetdirection):
	if (result_up == null || result_down == null || result_right == null ||result_left == null):
		return Vector2(0, 0)
	return targetdirection


func _get_random_direction():
	pass
	
	
	
