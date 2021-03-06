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

			//$command = isset($_GET['cmd']) ? $_GET['cmd'] : '';
			
			// if logged in..maybe can get user info (code portion in testing)
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            //get inputs for list info
            $list_title = isset($_GET['title']) ? $_GET['title'] : '';
            //$isDeleted = isset($_GET['isDeleted']) ? $_GET['isDeleted'] : '';  // place holder for later assignment
			//$listID = isset($_GET['listID']) ? $_GET['listID'] : '';  // place holder for later assignment
            $userID = isset($_GET['userID']) ? $_GET['userID'] : '';

            //build select query to obtain that user's station data
            $sql = "SELECT * FROM users ";
            $sql .= " WHERE email = '$email'";
    
			$stmt = $db->prepare($sql); 
			$stmt->execute();

			// set the resulting array to associative
			$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
			
            ///////////////////////////////////////////////////////////////////////////////////
            // check result and obtain userID
            if ($result != false) {
                //on success, return the user id
			   echo '{"result": "success", "userID": "' . $userID . '"}';
			   //$userID = $result['userID'];
            } else {
                echo '{"result": "fail", "error": "Incorrect email query to obtain userID."}';
            }
            ///////////////////////////////////////////////////////////////////////////////////
			
			//if ($command == "update") { 
			//$update_sql = "UPDATE lists SET title = ".'$list_title'.", isDeleted =".'$isDeleted'." WHERE listID = ".'$listID'";
			
			//}
			//else {
            if (strlen($list_title) < 1) {
                echo '{"result": "fail", "error": "Please enter a title for your list."}';
            } else {    
                //build query for list
                $sql = "INSERT INTO lists";
                $sql .= " VALUES (DEFAULT, '$list_title', 'NULL', current_timestamp(), current_timestamp())";
				
                //attempts to add list
                if ($db->query($sql)) {
                    echo '{"result": "successfully created a record for table: lists "}';
					
                    //obtains listID that was just stored
                    $listID = $db->lastInsertId();
                                        
                    //build query for listRights
                    $sql = "INSERT INTO listRights";
                    $sql .= " VALUES ('O', '$listID', '$userID')";
                 
                    //attempts to add listRights
                    if ($db->query($sql)) {
                        echo '{"result": "successfully created a record for table: listRights"}';
                    } 
                }
            }  
			// }		
			$db = null;			
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That list already exists."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
