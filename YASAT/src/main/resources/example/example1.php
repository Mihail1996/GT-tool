<?php

function first($dummy_id)
{


    $value = 132 * 123;
 //   $test = first(another_function($value, $stmt_value) . "1231");
    first(false);
    first("string". 1111);

    if (isset($_GET['id'])) {
        $id = $dummy_id;
        $another = first($id);
        $id = $_GET['id'];
        $var_with_const = "value";

        first(isset($_GET["asd"]));
        $mysqli = new mysqli('localhost', 'dbuser', 'dbpasswd', 'sql_injection_example');

        if ($mysqli->connect_errno) {
            printf("Connect failed: %s\n", $mysqli->connect_error);
            exit();
        }

        $sql = "SELECT username FROM users WHERE id = $id";

        if ($result = $mysqli->query($sql)) {
            while ($obj = $result->fetch_object()) {
                print($obj->username);
            }
        } elseif ($mysqli->error) {
            print($mysqli->error);
        }
    }
    return 0;
}

function another_function($input, $expr)
{
    return 0;
}

$exx = 123;
first($exx);

