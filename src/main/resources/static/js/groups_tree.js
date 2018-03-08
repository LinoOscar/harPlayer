$(document).ready(function() {
	
	// Add Resgroup form
	var $formRG = $('#addResGroup');
	$formRG.on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			url : $formRG.attr('action'),
			type : 'post',
			data : $formRG.serialize(),
			success : function(response) {
				// if the response contains any errors, replace the form
				if ($(response).find('.has-error').length) {
					// TODO: Mostrar el pete
					// $form.replaceWith(response);
				} else {
					$formRG.trigger("reset");
					$('#addResGroupModal').modal('hide');
					$('#resGroupList').replaceWith(response);
				}
			}
		});
	});

	
	// Update Card description
	var $formUD = $('#updateDescription');
	$formUD.on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			url : $formUD.attr('action'),
			type : 'post',
			data : $formUD.serialize(),
			success : function(response) {
				// if the response contains any errors, replace the form
				if ($(response).find('.has-error').length) {
					// TODO: Mostrar el pete
					// $form.replaceWith(response);
				} else {
					$formUD.trigger("reset");
					$('#addDescriptionModal').modal('hide');
					$('#cardDescription').replaceWith(response);
				}
			}
		});
	});

	// Add AccessInfo form
	var $formAI = $('#addAccessInfo');
	$formAI.on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			url : $formAI.attr('action'),
			type : 'post',
			data : $formAI.serialize(),
			success : function(response) {
				// if the response contains any errors, replace the form
				if ($(response).find('.has-error').length) {
					// TODO: Mostrar el pete
					// $form.replaceWith(response);
				} else {
					$formAI.trigger("reset");
					$('#addAccessInfoModal').modal('hide');
					$('#accessInfoList').replaceWith(response);
				}
			}
		});
	});

	// Add Contact form
	var $formC = $('#addContact');
	$formC.on('submit', function(e) {
		e.preventDefault();
		$.ajax({
			url : $formC.attr('action'),
			type : 'post',
			data : $formC.serialize(),
			success : function(response) {
				// if the response contains any errors, replace the form
				if ($(response).find('.has-error').length) {
					// TODO: Mostrar el pete
					// $form.replaceWith(response);
				} else {
					$formC.trigger("reset");
					$('#addContactModal').modal('hide');
					$('#contactList').replaceWith(response);
				}
			}
		});
	});
});

function removeResGroup(cardid, groupid) {	
	$.ajax({
		url : "/groups_tree/delete_res_group/"+cardid+"/"+groupid,
		type : 'post',
		success : function(response) {
			// if the response contains any errors, replace the form
			if ($(response).find('.has-error').length) {
				// TODO: Mostrar el pete
				// $form.replaceWith(response);
			} else {
				$('#resGroupList').replaceWith(response);
			}
		}
	});
};	

function removeContact(cardid, contactid) {	
	$.ajax({
		url : "/groups_tree/delete_contact/"+cardid+"/"+contactid,
		type : 'post',
		success : function(response) {
			// if the response contains any errors, replace the form
			if ($(response).find('.has-error').length) {
				// TODO: Mostrar el pete
				// $form.replaceWith(response);
			} else {
				$('#contactList').replaceWith(response);
			}
		}
	});
};	

function removeAccessData(cardid, accessinfoid) {	
	$.ajax({
		url : "/groups_tree/delete_access_info/"+cardid+"/"+accessinfoid,
		type : 'post',
		success : function(response) {
			// if the response contains any errors, replace the form
			if ($(response).find('.has-error').length) {
				// TODO: Mostrar el pete
				// $form.replaceWith(response);
			} else {
				$('#accessInfoList').replaceWith(response);
			}
		}
	});
};	

function updateAccessInfo(accessinfoid) {
	$('#addAccessInfoModal').modal('show');
	$('#accessInfoIdInpt').val(accessinfoid);
	$('#accessInfoTypeInpt').val($('#accessinfo_'+accessinfoid+'_type').text());
	$('#accessInfoUrlInpt').val($('#accessinfo_'+accessinfoid+'_url').text());	
	$('#accessInfoIpInpt').val($('#accessinfo_'+accessinfoid+'_ip').text());
	$('#accessInfoCredentialsInpt').val($('#accessinfo_'+accessinfoid+'_credentials').text());
	$('#accessInfoMachinesInpt').val($('#accessinfo_'+accessinfoid+'_machines').text());
	$('#accessInfoDescriptionInpt').val($('#accessinfo_'+accessinfoid+'_description').text());
	$('#accessInfoDBInfoInpt').val($('#accessinfo_'+accessinfoid+'_dbinfo').text());
}

function updateContact(contactid) {
	$('#addContactModal').modal('show');
	$('#contactIdInpt').val(contactid);
	$('#contactNameInpt').val($('#contact_'+contactid+'_name').text());
	$('#contactPhoneInpt').val($('#contact_'+contactid+'_phone').text());	
	$('#contactMobileInpt').val($('#contact_'+contactid+'_mobile').text());
	$('#contactEmailInpt').val($('#contact_'+contactid+'_email').text());
	$('#contactAddressInpt').val($('#contact_'+contactid+'_address').text());
	$('#contactDescriptionInpt').val($('#contact_'+contactid+'_description').text());
}
