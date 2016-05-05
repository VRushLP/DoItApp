<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);

	    // Connect to the Database
	    $dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
    	$username = '_450atm10';
    	$password = 'afimmit';
       
    	try {
        	$db = new PDO($dsn, $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //get inputs for course information
            $taskInput = isset($_GET['textInput']) ? $_GET['textInput'] : '';
  
            /*
            //////////////////////////////////////////////////////////////////////////////////////////
            // if logged in..maybe can get user info (code portion in testing)
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            
            //build select query
            $sql = "SELECT userID FROM users ";
            $sql .= " WHERE email = '" . $email . "'";

    
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetch(PDO::FETCH_ASSOC);
            
   
            //check results
            if ($result != false) {
                //on success, return the user id
                if (strcmp($pwd, $result['pwd']) == 0)
                   echo '{"result": "success", "userID": "' . $result['userID'] . '"}';
                   $userID = $result['userID'];
               else 
                  echo '{"result": "fail", "error": "Incorrect email to obtain userID."}';
            } else {
                echo '{"result": "fail", "error": "Incorrect email query to obtain userID."}';
            }
            ///////////////////////////////////////////////////////////////////////////////////////////
            */
            
            
            if (strlen($taskInput) < 1) {
                echo '{"result": "fail", "error": "Please enter text for your task."}';
            } else {    
                //build query for list
                $sql = "INSERT INTO tasks";
                $sql .= " VALUES ('$taskInput', 'NULL')";
                
                //attempts to add list
                if ($db->query($sql)) {
                    echo '{"result": "successfully created a record for table: tasks"}';
                
                    //obtains listID that was just stored
                    $taskID = mysqli_insert_id($db);
                    
                    /*$sql = "SELECT userID FROM users ";
                    $sql .= " WHERE email = '" . $email . "'";

                    $q = $db->prepare($sql);
                    $q->execute();
                    $result = $q->fetch(PDO::FETCH_ASSOC);*/
                    
                    //build query for listRights
                    $sql = "INSERT INTO taskRights";
                    $sql .= " VALUES ('O', '$taskID', '$listID')";
                 
                    //attempts to add listRights
                    if ($db->query($sql)) {
                        echo '{"result": "successfully created a record for table: taskRights"}';
                        $db = null;
                    } 
                }
            }   
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That task already exists."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
