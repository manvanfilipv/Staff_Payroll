function fix_start_date() {

    var st_date = document.getElementById("start_date_add").value;
    var res = st_date.split("-", 3);
    let day = res[0];
    let month = res[1];
    let years = res[2];
    var d = new Date(res[2], res[1], res[0]);

    if (isNaN(d.getTime()))
    {
        console.log("Invalid Date.");
    } else {
        console.log("Valid Date.");
    }

    if (day === "01" || day === "1") {
        console.log("einai 1h tou mhna!");
    } else if ((day !== "01" && month !== "12") || (day !== "1" && month !== "12")) { //15/1/2020
        day = "01";
        month = (Number(month) + 1);
        if (month < 10)
            month = "0" + month;
        st_date = day + "-" + month + "-" + years;
        console.log(st_date);
    } else if ((day !== "01" && month === "12") || (day !== "1" && month === "12")) { //15/12/2020
        day = "01";
        month = "01";
        years = Number(years) + 1;
        st_date = day + "-" + month + "-" + years;
        console.log(st_date);
    }
    document.getElementById("start_date_add").value = st_date;
    check_end_date();
}

function check_end_date() {
    var st_date = String(document.getElementById("start_date_add").value);
    var en_date = String(document.getElementById("end_date_add").value);

    console.log(st_date);
}

function getAge(dateString) {
    var today = new Date();
    var res = dateString.split("-", 3);
    var birthDate = new Date(res[1] + "/" + res[0] + "-" + res[2]);
    var age = today.getFullYear() - birthDate.getFullYear();
    console.log(age);
    var m = today.getMonth() - birthDate.getMonth();
    console.log(m);
    console.log("tday month" + today.getMonth());
    console.log("stday month" + birthDate.getMonth());

    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
        console.log("llalal");
    }
    return age;
}

function AddUser() {
    fix_start_date();
    let ages = "";
    for (var i = 0; i < 10; i++) {
        if (typeof ($("#child" + i).val()) !== "undefined") {
            ages += $("#child" + i).val() + ",";
        } else {
            break;
        }
    }

    ages = ages.slice(0, ages.lastIndexOf(","));
    var data = {
        fullname: xssShield($("#first_add").val() + " " + $("#last_add").val()),
        address: xssShield($("#address_add").val()),
        bank: xssShield($("#bank_add").val()),
        category: xssShield($("#category_add").val()),
        children: xssShield($('#children_add').val()),
        year_children: xssShield(ages),
        department: xssShield($("#department_add").val()),
        IBAN: xssShield($("#iban_add").val()),
        start_date: xssShield($("#start_date_add").val()),
        end_date: xssShield($("#end_date_add").val()),
        status: xssShield($("#status_add").val()),
        phone: xssShield($("#telephone_add").val()),
        basic: xssShield($("#pay_add").val())
    };

    $.ajax({
        type: 'POST',
        url: 'AddUser',
        data: data,
        async: false,
        success: function (data) {
            if (data !== ("")) {
                $("#results_add").html("<b>" + "#DB: Employee Successfully added!" + "</b>");
                // Set Wage Category
                let category_wage = $("#category_add").val();
                let status_wage = $("#status_add").val();
                let years_wage = "0";
                let st_date = $("#start_date_add").val();
                let res = st_date.split("-", 3);
                let basic_pay;
                if (category_wage === "Permanent Teacher" || category_wage === "Permanent Admin") {
                    basic_pay = "";

                } else {
                    basic_pay = $("#pay_add").val();

                }
                $("#pay_add").val("");
                console.log(st_date);
                if (getAge(st_date) >= 0 && (category_wage === "Permanent Teacher" || category_wage === "Permanent Admin")) {
                    years_wage = getAge(st_date);
                } else {
                    years_wage = -1;
                }
                console.log(basic_pay);
                console.log(typeof (basic_pay));
                // Add Pay-Wage with specific ID
                AddPayWage(data.trim(), category_wage, status_wage, String(years_wage), basic_pay);

            } else
                $("#results_add").html("<b>" + data + "</b>");
        }
    });
}
function AddPayWage(id, cat, stat, years, pay) {

    var data = {
        userid: id,
        category: xssShield(cat),
        status: xssShield(stat),
        years: xssShield(years),
        pay: xssShield(pay)
    };

    $.ajax({
        type: 'POST',
        url: 'AddPayWage',
        data: data,
        async: false,
        success: function (data) {
            if (data !== (""))
                $("#results_add").html("<b>" + data + "</b>");
            else
                $("#results_add").html("<b>" + "#DB: Employee's Payroll + Pay addition failed - Not the 1st of the Month." + "</b>");
        }
    });
}

function Delete() {

    var data = {
        userid: xssShield($("#userid_fire").val())
    };

    $.ajax({
        type: 'POST',
        url: 'DeleteUser',
        data: data,
        async: false,
        success: function (data) {
            if (data === ("#DB: Employee Successfully deleted!"))
                $("#results_fire").html("<b>" + data + "</b>");
            else
                $("#results_fire").html("<b>" + data + "</b>");
        }
    });
}

function UpdateUser() {

    //fix_start_date();
    let ages = "";
    for (var i = 0; i < 10; i++) {
        if (typeof ($("#child_edit" + i).val()) !== "undefined") {
            ages += $("#child_edit" + i).val() + ",";
        } else {
            break;
        }
    }
    console.log(ages);
    // Chop last comma
    ages = ages.slice(0, ages.lastIndexOf(","));

    var data = {
        userid: xssShield($("#userid_edit").val()),
        address: xssShield($("#address_edit").val()),
        bank: xssShield($("#bank_edit").val()),
        children: xssShield($("#children_edit").val()),
        year_children: xssShield(ages),
        department: xssShield($("#department_edit").val()),
        IBAN: xssShield($("#iban_edit").val()),
        status: xssShield($("#status_edit").val()),
        phone: xssShield($("#telephone_edit").val())
    };

    $.ajax({
        type: 'POST',
        url: 'UpdateUser',
        data: data,
        async: false,
        success: function (data) {
            $("#results_edit").html("<b>" + data + "</b>");
        }
    });
}

function Promote() {

    var data = {
        userid: xssShield($("#userid_promote").val())
    };

    $.ajax({
        type: 'POST',
        url: 'PromoteUser',
        data: data,
        async: false,
        success: function (data) {
            if (data === ("#DB: User Promoted Successfully."))
                $("#results_promote").html("<b>" + data + "</b>");
            else
                $("#results_promote").html("<b>" + data + "</b>");
        }
    });
}

function ChangeBonus() {

    var data = {
        change: xssShield($("#dropdown_bonus").val()),
        value: xssShield($("#value_bonus").val())
    };

    $.ajax({
        type: 'POST',
        url: 'ChangeBonus',
        data: data,
        async: false,
        success: function (data) {
            if (data === ("#DB: Change was successfully made."))
                $("#results_bonus").html("<b>" + data + "</b>");
            else
                $("#results_bonus").html("<b>" + data + "</b>");
        }
    });
}

function GetMaxMinAvg() {

    var data = {
        category: xssShield($("#category_MaxMinAvg").val())
    };

    $.ajax({
        type: 'POST',
        url: 'GetMaxMinAvg',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_MaxMinAvg").html(data);
            else
                $("#results_MaxMinAvg").html("No Users under this category");
        }
    });
}

function GetUserWage() {

    var data = {
        userid: xssShield($("#userid_user_wage").val())
    };

    $.ajax({
        type: 'POST',
        url: 'GetUserWage',
        data: data,
        async: false,
        success: function (data) {
            $("#results_user_wage").html(data);
        }
    });
}

function PayPerCategory() {

    var data = {
        category: xssShield($("#category_pay_per_category").val())
    };

    $.ajax({
        type: 'POST',
        url: 'PayPerCategory',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_pay_per_category").html(data);
            else
                $("#results_pay_per_category").html("No Users under this category");

        }
    });
}

function StatusPayPerCategory() {

    var data = {
        category: xssShield($("#category_status_pay_per_category").val())
    };

    $.ajax({
        type: 'POST',
        url: 'StatusPayPerCategory',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_status_pay_per_category").html(data);
            else
                $("#results_status_pay_per_category").html("No Users under this category");
        }
    });
}

function StatusPayPerMonth() {

    //$("#start_status_pay_per_category").val("01-01-2020");
    //$("#end_status_pay_per_category").val("05-06-2020");
    var data = {
        start: xssShield($("#start_status_pay_per_category").val()),
        end: xssShield($("#end_status_pay_per_category").val())
    };

    $.ajax({
        type: 'POST',
        url: 'StatusPayForMonths',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_status_pay_for_months").html(data);
            else
                $("#results_status_pay_for_months").html("No Payments were made during this period.");
        }
    });
}

function SumPay() {

    var data = {
        category: xssShield($("#sum_pay_per_category").val())
    };

    $.ajax({
        type: 'POST',
        url: 'SumPay',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_sum_pay_per_category").html(data);
            else
                $("#results_sum_pay_per_category").html("No Users under this category");
        }
    });
}

function SQL_Query() {

    var data = {
        sql: $("#question_sql_query").val()
    };

    $.ajax({
        type: 'POST',
        url: 'SQL',
        data: data,
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_question_sql_query").html(data);
            else
                $("#results_question_sql_query").html("Invalid Input Syntax");
        }
    });
}

function UpdateYears() {

    $.ajax({
        type: 'GET',
        url: 'UpdateYears',
        async: false,
        success: function (data) {
            if (data !== "<b>Updated Years of Users with IDs </b><br>")
                $("#results").html(data);
            else
                $("#results").html("No Year-Updates needed.");
        }
    });
}

function GetEmployees() {

    $.ajax({
        type: 'GET',
        url: 'GetEmployees',
        async: false,
        success: function (data) {
            if (data !== "")
                $("#results_get_employees").html(data);
            else
                $("#results_get_employees").html("No Users in the Database");
        }
    });
}

function xssShield(str) {
    if (typeof str !== "string") {
        return;
    }
    return str.replace(/<|>|"|'/gi, "");
}