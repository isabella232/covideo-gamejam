extends HTTPRequest

export var world_gen_url = "http://localhost:8080/backend"
export var level_generate_path = "/level/generate"

func level_generate(data):
	return _json_request(world_gen_url + level_generate_path, JSON.print(data))
	
func _json_request(req_url, data):
	var headers = ["Accept: */*", "Content-Type: application/json"]
	var err = request(req_url, headers, false, HTTPClient.METHOD_POST, data)
	if err != OK: return printerr("request error {}".format(err))
	var res = yield(self, "request_completed")
	if res[1] != 200: return printerr("reponse is not 200 OK but {1} {3}".format(res))
	var body = res[3].get_string_from_utf8()
	var json = JSON.parse(body)
	if json.error: return printerr("response error {0} {1} {2}, got {3}".format([json.error, json.error_string, json.error_line, body]))
	return json.result
