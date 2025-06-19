
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

    var errorAlert = document.querySelector('.alert-danger');
    var successAlert = document.querySelector('.alert-success');
        var warnAlert = document.querySelector('.alert-warning');

    if (errorAlert) {
        setTimeout(function () {
            errorAlert.style.visibility = 'hidden';
        }, 3000);
    }

    if (successAlert) {
        setTimeout(function () {

            successAlert.style.visibility = 'hidden';
        }, 3000);
    }
     if (warnAlert) {
        setTimeout(function () {

            warnAlert.style.visibility = 'hidden';
        }, 3000);
    }
});



