<?php

function first(){
    if (isset($_GET['id'])) {
        $id = $_GET['id'];

        $mysqli = new mysqli('localhost', 'dbuser', 'dbpasswd', 'sql_injection_example');

        /* Check connection before executing the SQL query */
        if ($mysqli->connect_errno) {
            printf("Connect failed: %s\n", $mysqli->connect_error);
            exit();
        }

        $sql = "SELECT username FROM users WHERE id = $id";

        /* Select queries return a result */
        if ($result = $mysqli->query($sql)) {
            while ($obj = $result->fetch_object()) {
                print($obj->username);
            }
        } elseif ($mysqli->error) {
            print($mysqli->error);
        }
    }
}

first();

