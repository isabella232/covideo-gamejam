extends Node

var viewport_size

func _ready():
	viewport_size = get_viewport().size
	
	
	var npc_old_scene_resource = load("res://scenes/NPC_retired_person.tscn")
	var npc_yolo_scene_resource = load("res://scenes/NPC_yolo_hipster.tscn")
	var npc_mum_scene_resource = load("res://scenes/NPC_family_mum.tscn")

	var npc_old_scene = npc_old_scene_resource.instance()
	var npc_yolo_scene = npc_yolo_scene_resource.instance()
	var npc_mum_scene = npc_mum_scene_resource.instance()
	add_child(npc_old_scene)
	add_child(npc_yolo_scene)
	add_child(npc_mum_scene)
	
	npc_old_scene.global_position = Vector2(5, -50)
	npc_yolo_scene.global_position = Vector2(55, 40)
	npc_mum_scene.global_position = Vector2(-50, 65)

	var tpr_scene_resource = load("res://scenes/ToiletPaperRoll.tscn")
	for i in range(0, 10):
		var tpr_scene = tpr_scene_resource.instance()
		add_child(tpr_scene)
		tpr_scene.global_position = Vector2(100 - (30 * i), -30 + (30 * i))
		tpr_scene.add_to_group("tprs")
		get_tree().call_group("npcs", "_add_target", tpr_scene)
		
		
		
