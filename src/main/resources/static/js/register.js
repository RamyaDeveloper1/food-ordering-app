
document.addEventListener('DOMContentLoaded', function () {
    var password = document.getElementById("password");
    var confirmPassword = document.getElementById("confirmPassword");
    var message = document.getElementById("passwordError");

    function validatePassword() {
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmPassword").value;
        var message = document.getElementById("passwordError");

        if (password !== confirmPassword) {
            message.style.display = "block";
            message.textContent = "Passwords do not match!";
            return false;
        } else {
            message.style.display = "none";
            return true;
        }
    }
    var errorAlert = document.querySelector('.alert-danger');
    var successAlert = document.querySelector('.alert-success');

    if (errorAlert) {
        setTimeout(function () {

            errorAlert.style.visibility='hidden';
        }, 2000);
    }

    if (successAlert) {
        setTimeout(function () {

            successAlert.style.visibility = 'hidden';
        }, 2000);
    }

       var form = document.querySelector('form');
      form.addEventListener('submit', function (event) {
        if (!validatePassword()) {
            event.preventDefault();
        }
    });
});
