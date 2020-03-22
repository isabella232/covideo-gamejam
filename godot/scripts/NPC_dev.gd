extends Node

var viewport_size

func _ready():
	viewport_size = get_viewport().size
	
	var player_scene_resource = load("res://scenes/Player.tscn")
	var player_scene = player_scene_resource.instance()
	add_child(player_scene)
	player_scene.get_child(0).position = Vector2(viewport_size.x / 2, viewport_size.y / 2)

	var npc_old_scene_resource = load("res://scenes/NPC_retired_person.tscn")
	var npc_yolo_scene_resource = load("res://scenes/NPC_yolo_hipster.tscn")
	var npc_mum_scene_resource = load("res://scenes/NPC_family_mum.tscn")

	var npc_old_scene = npc_old_scene_resource.instance()
	var npc_yolo_scene = npc_yolo_scene_resource.instance()
	var npc_mum_scene = npc_mum_scene_resource.instance()
	add_child(npc_old_scene)
	add_child(npc_yolo_scene)
	add_child(npc_mum_scene)
