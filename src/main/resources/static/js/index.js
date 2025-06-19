document.addEventListener('DOMContentLoaded', function () {
 
    const logoutTrigger = document.getElementById('logout-trigger');
const logoutPopover = document.getElementById('logout-popover');
const cancelLogout = document.getElementById('cancel-logout');

const deleteAccountTrigger = document.getElementById('delete-account-trigger');
const deleteAccountPopover = document.getElementById('delete-account-popover');
const cancelDelete = document.getElementById('cancel-delete');

document.addEventListener('click', function (e) {
    if (e.target === logoutTrigger) {
        e.preventDefault();
        logoutPopover.classList.remove('d-none');
    } else if (e.target === cancelLogout) {
        logoutPopover.classList.add('d-none');
    } else if (e.target === deleteAccountTrigger) {
        e.preventDefault();
        deleteAccountPopover.classList.remove('d-none');
    } else if (e.target === cancelDelete) {
        deleteAccountPopover.classList.add('d-none');
    }
});

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
            errorAlert.style.display = 'none';
        }, 3000);
    }

    if (successAlert) {
        setTimeout(function () {
            successAlert.style.display = 'none';
        }, 3000);
    }
});
