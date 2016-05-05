    <?php
    /* 
     * This file provides a web service to authenticate a user. Given "email" and "pwd",
     *  attempts to authenticate the user. If successful, returns the user ID. Else, returns
     *  an error message.
     *
     */
     
    ini_set('display_errors', '1');
    error_reporting(E_ALL);

            //get input
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $pwd = isset($_GET['pwd']) ? $_GET['pwd'] : '';

            // Connect to the Database
            $dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
            $username = '_450atm10';
            $password = 'afimmit';
           
            try {
                $db = new PDO($dsn, $username, $password);
                $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                
                //validate input
                if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                    echo '{"result": "fail", "error": "Please enter a valid email."}';
                } else if (strlen($pwd) < 6) {
                    echo '{"result": "fail", "error": "Please enter a valid password (longer than five characters)."}';
                } else {    
                    //build query
                    $sql = "SELECT email, pwd FROM users ";
                    $sql .= " WHERE email = '" . $email . "'";

            
                    $q = $db->prepare($sql);
                    $q->execute();
                    $result = $q->fetch(PDO::FETCH_ASSOC);
                    
           
                    //check results
                    if ($result != false) {
                        //on success, return the user id
                        if (strcmp($pwd, $result['pwd']) == 0)
            	           echo '{"result": "success", "email": "' . $result['email'] . '"}';
    	               else 
    		              echo '{"result": "fail", "error": "Incorrect password."}';
                    } else {
                        echo '{"result": "fail", "error": "Incorrect email."}';
                    }
                }
                
            }
            catch (PDOException $e) {
                echo 'Error Number: ' . $e->getCode() . '<br>';
            }

            
    ?>