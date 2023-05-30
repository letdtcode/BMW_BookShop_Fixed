function validateFormRegister(event) {
    event.preventDefault();
    let password = $('#inputPassword').val();
    let validatePasswordShow = $('#validatePassword');
    let passwordRegex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$/;
    if (!passwordRegex.test(password)) {
        validatePasswordShow.html("Mật khẩu không đáp ứng yêu cầu: ít nhất 8 kí tự, có chữ hoa, chữ thường, kí tự đặc biệt và tối đa 15 kí tự.").css('color','red');
        return false;
    } else {
        validatePasswordShow.html('');
    }
    $('#form-register').unbind('submit').submit();

}

function validateFormChangePassword(event) {
    event.preventDefault();
    let password = $('#inputNewPassword').val();
    let passwordConfirm = $('#inputNewPasswordAgain').val();

    let validatePasswordShow = $('#validatePassword');
    let validatePasswordMatchShow = $('#validatePasswordMatch');

    let isEqual = password.localeCompare(passwordConfirm);
    let passwordRegex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$/;

    if (!passwordRegex.test(password)) {
        validatePasswordShow.html("Mật khẩu không đáp ứng yêu cầu: ít nhất 8 kí tự, có chữ hoa, chữ thường, kí tự đặc biệt và tối đa 15 kí tự.").css('color','red');
        return false;
    } else {
        validatePasswordShow.html('');
    }
    if (isEqual !== 0) {
        validatePasswordMatchShow.html("Mật khẩu không khớp").css('color','red');
        return false;
    }
    else {
        validatePasswordMatchShow.html('');
    }
    $('#form-change-pass').unbind('submit').submit();
}



