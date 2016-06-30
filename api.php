<?php

header('Content-Type: application/json');
if (isset($_GET['action'])) {
	if ($_GET['action'] == 'device_register') {
		if (isset($_POST['token'])) {
			$token = $_POST['token'];
			// If token exists, do nothing, if it does not, add it to the table
			getJson("DO \$do$ BEGIN IF NOT EXISTS (SELECT 1 FROM token where token = '" . $token . "') THEN INSERT INTO token values('$token'); END IF; END \$do$");
			echo '{"success": true}';
		}
		else {
			echo '{"success": false, "message": "Wrong variables for action suplied"}';
		}
	}
	else if ($_GET['action'] == 'message_send') {
		if (isset($_POST['message']) && isset($_POST['userId'])) {
			$message = $_POST['message'];
			$userId = $_POST['userId'];

			$data = array('message' => $message, 'userId' => $userId);

		    $db = getDatabase();
		    $ret = pg_query($db, "SELECT token from token;");
		    if(!$ret){
		        echo pg_last_error($db);
		        exit;
		    }
		    $ids = array();
		    while($row = pg_fetch_row($ret)) {
		    	array_push($ids, $row[0]);
		    }

		    $ret = pg_query($db, "SELECT * from korisnik");
		    if(!$ret){
		        echo pg_last_error($db);
		        exit;
		    }
		    $users = "";
		    while($row = pg_fetch_assoc($ret)) {
		    	if ($users != "") {
		    		$users .= $row['id'];
		    	}
		    }

		    $ret = pg_query($db, "INSERT into message values (default, " . $message . ", " . $userId . ", 1,{" . $users . "}, {}, {}");
		    if(!$ret){
		        echo pg_last_error($db);
		        exit;
		    }

		    pg_close($db);

		    $resultArray = json_decode(sendPushNotification($data, $ids), true);

		    $success = $resultArray['success'];
		    $fail = $resultArray['failure'];

		    if ($success >= 1) {
		    	echo '{"success": true, "message": "Success: ' . $success . ',Fail: ' . $fail . '"}';
		    }
		    else {
		    	echo '{"success": false, "message": "Something went wrong"}';
		    }
		}
		else {
			echo '{"success": false, "message": "Wrong variables for action suplied"}';
		}
	}
	else if ($_GET['action'] == 'user_register') {
		if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['imageUrl'])) {
			$name = $_POST['name'];
		    $email = $_POST['email'];
		    $password = $_POST['password'];
		    $imageUrl = $_POST['imageUrl'];

		    $db = getDatabase();
		    $ret = pg_query($db, "SELECT count(*) from korisnik where email = '$email'");
		    if(!$ret){
		        echo pg_last_error($db);
		        exit;
		    }

		    $result = pg_fetch_assoc($ret);

		    if ($result['count'] == 1) {
		    	echo '{"success": false, "message": "More than one user is registered with same password"}';
		    }
		    else {
		    	$db = getDatabase();
		    	$ret = pg_query("INSERT INTO korisnik values (default, '$name', '$email', '$password', '$imageUrl') returning id");
		    	$result = pg_fetch_assoc($ret);
		    	$id = $result['id'];

		    	addUserToChat($id, 1);
		    	echo '{"success": true, "data": {"id": ' . $id . '}}';
		    }
		}
		else {
			echo '{"success": false, "message": "Wrong variables for action suplied"}';
		}
	}
	else if ($_GET['action'] == 'user_login') {
		if (isset($_POST['email']) && isset($_POST['password'])) {
			$email = $_POST['email'];
		    $password = $_POST['password'];

		    $db = getDatabase();
		    $ret = pg_query($db, "SELECT id from korisnik where email = '$email' and password = '$password'");
		    if(!$ret){
		        echo pg_last_error($db);
		        exit;
		    }

		    $result = pg_fetch_assoc($ret);

		    if ($result['id'] == null) {
		    	echo '{"success": false, "message": "Wrong username or password"}';
		    }
		    else {
		    	echo '{"success": true, "data": ' . $result['id'] . '}';
		    }

		    pg_close($db);
		}
		else {
			echo '{"success": false, "message": "Wrong variables for action suplied"}';
		}
	}
	else if ($_GET['action'] == 'user_get') {
		if (isset($_POST['userId'])) {
    		$id = $_POST['userId'];
    		echo getJson("SELECT id, name, email, image FROM korisnik where id = $id");
    	}
		else {
			echo '{"success": false, "message": "Wrong variables for action suplied"}';
		}
	}
	else if ($_GET['action'] == 'users_get') {
		echo getListJson("SELECT id, name, email, image FROM korisnik");
	}
	else {
		echo '{"success": false, "message": "Suplied action doesn\'t exist"}';
	}
}
else {
	echo '{"success": false, "message": "No action suplied"}';
}

function sendPushNotification($data, $ids)
{
    $apiKey = 'AIzaSyC3mvvLnGdm0KElCOpBXcTL5rLrhGCTTrc';

    $post = array(
                    'registration_ids'  => $ids,
                    'data'              => $data,
                 );

    $headers = array( 
                        'Authorization: key=' . $apiKey,
                        'Content-Type: application/json'
                    );
  
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'https://gcm-http.googleapis.com/gcm/send');
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);   
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($post));

    return $result = curl_exec($ch);
}

function getListJson($sql) {
	$db = getDatabase();
	$ret = pg_query($db, $sql);
	if(!$ret){
		echo pg_last_error($db);
		exit;
	}
	$myarray = array();
	while ($row = pg_fetch_assoc($ret)) {
		$myarray[] = $row;
	}
	pg_close($db);
	return json_encode($myarray, JSON_NUMERIC_CHECK);
}

function getJson($sql) {
    $db = getDatabase();
    $ret = pg_query($db, $sql);
    if(!$ret){
        echo pg_last_error($db);
        exit;
    }
    $result = pg_fetch_assoc($ret);
    pg_close($db);
    return json_encode($result, JSON_NUMERIC_CHECK);
}

function getDatabase() {
    $host = "ec2-54-247-185-241.eu-west-1.compute.amazonaws.com";
    $user = "syiufhlglgivgs";
    $pass = "7bPNExVVA4Cqbb3G_tRNC_-J2c";
    $dbname = "d8o6rshdokm2j7";
    $connectionString = "host=$host port=5432 dbname=$dbname user=$user password=$pass";
    return pg_connect($connectionString);
}

function addUserToChat($id, $chat) {
	$db = getDatabase();
	$ret = pg_query($db, "UPDATE chat set users = users || " . $id . " where id = " . $chat);
}

?>