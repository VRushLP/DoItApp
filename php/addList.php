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

			// if logged in..maybe can get user info (code portion in testing)
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            //get inputs for list info
            $list_title = isset($_GET['title']) ? $_GET['title'] : '';
            
            $userID = isset($_GET['userID']) ? $_GET['userID'] : '';  // place holder for later assignment
			
            //build select query to obtain that user's station data
            $sql = "SELECT * FROM users ";
            $sql .= " WHERE email = '$email'";
    
            #$q = $db->prepare($sql);
            #$q->execute();
            #$result = $q->fetch(PDO::FETCH_ASSOC);
			
			#$q = $db->query($sql);
			#$q->execute();
			#$result = $q->fetch(PDO::FETCH_ASSOC);
			
			$stmt = $db->prepare($sql); 
			$stmt->execute();

			// set the resulting array to associative
			$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
			
            
			//////////////////////////////////////////////////////////////////////////////////
            //check results
            #if ($result != false) {
            #    //on success, return the user id
            #    if (strcmp($pwd, $result['pwd']) == 0)
            #       echo '{"result": "success", "userID": "' . $result['userID'] . '"}';
            #       $userID = $result['userID'];
            #   else 
            #      echo '{"result": "fail", "error": "Incorrect email to obtain userID."}';
            #} else {
            #    echo '{"result": "fail", "error": "Incorrect email query to obtain userID."}';
            #}
            ///////////////////////////////////////////////////////////////////////////////////
            // check result and obtain userID
            if ($result != false) {
                //on success, return the user id
			   echo '{"result": "success", "userID": "' . $result['userID'] . '"}';
			   $userID = $result['userID'];
            } else {
                echo '{"result": "fail", "error": "Incorrect email query to obtain userID."}';
            }
            ///////////////////////////////////////////////////////////////////////////////////
			
			
            // retrieve lists portion using userID
            if (strlen($list_title) < 1) {
                echo '{"result": "fail", "error": "Please enter a title for your list."}';
            } else {    
                //build query for list
                $sql = "INSERT INTO lists";
                $sql .= " VALUES ('0', '$list_title', 'NULL')";
                
                //attempts to add list
                if ($db->query($sql)) {
                    echo '{"result": "successfully created a record for table: lists"}';
					
                    //obtains listID that was just stored
                    $listID = $db->lastInsertId();
                                        
                    //build query for listRights
                    $sql = "INSERT INTO listRights";
                    $sql .= " VALUES ('O', '$listID', '$userID')";
                 
                    //attempts to add listRights
                    if ($db->query($sql)) {
                        echo '{"result": "successfully created a record for table: listRights"}';
                        $db = null;
                    } 
                }
            }   
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That list already exists."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
