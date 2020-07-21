<?php

function first($id)
{

    $sql = "SELECT username FROM users WHERE id =  $id";

    $result = mysql_query($sql);

    return 0;
}

function another_function($input, $expr)
{
    return first("sdfs");
}

