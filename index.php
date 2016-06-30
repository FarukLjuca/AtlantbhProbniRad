<!doctype html>
<html>
    <head>
        <title>by Faruk</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
        <style>
            .form-control {
                margin: 10px 0;
                width: 40%;
                display: inline-block;
            }
        </style>
    </head>
    <body>
        <div class="container" style="margin-top: 100px; text-align: center;">
            <h1>Send notifications</h1>
            <form method="post">
                <input type="text" placeholder="Enter title" name="title" class="form-control" /><br>
                <input type="text" placeholder="Enter message" name="message" class="form-control" /><br>
                <input type="text" placeholder="Enter image url" name="image" class="form-control" /><br><br><br>
                <input type="submit" value="Send notification" name="submit" class="btn btn-primary" />
            </form>
            <?php

            if ($_POST['submit']) {
                $data = array('message' => $_POST['message'], 'title' => $_POST['title'], 'image' => $_POST['image']);

                $db = getDatabase();
                $ret = pg_query($db, "select token from token;");
                if(!$ret){
                    echo pg_last_error($db);
                    exit;
                }
                $ids = pg_fetch_array($ret, 0, PGSQL_NUM);
                pg_close($db);

                sendPushNotification($data, $ids);
            }

            ?>
        </div>

        <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    </body>
</html>
<?php

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

    $result = curl_exec($ch);
    if (curl_errno($ch))
    {
        echo 'GCM error: ' . curl_error($ch);
    }
    curl_close($ch);     
    echo "<div style='display: none;'>$result</div>";
    echo '<br><br><div class="alert alert-success" style="width: 30%; margin: 0 auto; text-align: center;"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Notification has been successfuly dalevered.</div>';
}

function getDatabase() {
    $host = "ec2-54-247-185-241.eu-west-1.compute.amazonaws.com";
    $user = "syiufhlglgivgs";
    $pass = "7bPNExVVA4Cqbb3G_tRNC_-J2c";
    $dbname = "d8o6rshdokm2j7";
    $connectionString = "host=$host port=5432 dbname=$dbname user=$user password=$pass";
    return pg_connect($connectionString);
}

?>

