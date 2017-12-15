#
# ~/.bashrc
#

# If not running interactively, don't do anything
[[ $- != *i* ]] && return

alias ls='ls --color=auto'
PS1='[\u@\h \W]\$ '

if [ -f /usr/lib/python3.4/site-packages/powerline/bindings/bash/powerline.sh ]; then
	    source /usr/lib/python3.4/site-packages/powerline/bindings/bash/powerline.sh
    fi
